package com.amin.pom.card

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.doAfterTextChanged
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.amin.pom.CardType
import com.amin.pom.R
import com.amin.pom.databinding.FragmentCardBinding
import com.amin.pom.utils.CreditCardNumberMask
import com.amin.pom.utils.expireDateDialog
import com.amin.pom.utils.setCompoundDrawables
import io.card.payment.CardIOActivity
import io.card.payment.CreditCard
import kotlinx.android.synthetic.main.fragment_card.*


class CardFragment : Fragment() {

  private val cardViewModel: CardViewModel by viewModels()

  private lateinit var binding: FragmentCardBinding

  private val launchScanCard = registerForActivityResult(
    ActivityResultContracts.StartActivityForResult()
  ) { result ->
    if (result.data != null) {
      val scanResult: CreditCard = result.data!!.getParcelableExtra(
        CardIOActivity.EXTRA_SCAN_RESULT
      )!!
      cardViewModel.cardNumber.value = scanResult.formattedCardNumber
      cardViewModel.expireDate.value = if (scanResult.isExpiryValid) {
        "${scanResult.expiryMonth}/${scanResult.expiryYear}"
      } else null
      cardViewModel.cvv.value = scanResult.cvv
    }
  }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    binding = DataBindingUtil.inflate(inflater, R.layout.fragment_card, container, false)
    binding.lifecycleOwner = viewLifecycleOwner
    binding.viewModel = cardViewModel
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    initCardNumberInputType()
    initObservers()

    binding.appCompatImageView.setOnClickListener {
      val scanIntent = Intent(requireContext(), CardIOActivity::class.java).apply {
        putExtra(CardIOActivity.EXTRA_REQUIRE_EXPIRY, true)
        putExtra(CardIOActivity.EXTRA_REQUIRE_CVV, false)
      }

      launchScanCard.launch(scanIntent)
    }
  }

  private fun initObservers() {
    cardViewModel.viewState.observe(viewLifecycleOwner) { state ->
      when (state) {
        is CardViewModel.ViewState.OpenExpireDialog -> {
          showCardExpireDialog(state.year, state.month)
        }
      }
    }
  }

  private fun initCardNumberInputType() {
    cardNumberInput.apply {
      addTextChangedListener(CreditCardNumberMask())
      doAfterTextChanged {
        it?.trim()?.toString()?.replace(" ", "")?.also(::setCardType)
      }
    }
  }

  private fun showCardExpireDialog(year: Int?, month: Int?) {
    expireDateDialog(
      requireContext(),
      year = year,
      month = month,
      onDateSet = { newYear, newMonth ->
        cardViewModel.expireDate.value = "$newMonth/$newYear"
      }
    )
  }

  private fun setCardType(cardNumber: String) {
    val typeDrawableRes = when (CardType.detect(cardNumber)) {
      CardType.UNKNOWN -> 0
      CardType.VISA -> R.drawable.visa
      CardType.MASTERCARD -> R.drawable.mc
    }
    cardNumberInput.setCompoundDrawables(right = typeDrawableRes)
  }
}