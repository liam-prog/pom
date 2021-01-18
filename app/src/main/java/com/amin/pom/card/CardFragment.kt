package com.amin.pom.card

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.amin.pom.CardType
import com.amin.pom.R
import com.amin.pom.databinding.FragmentCardBinding
import com.amin.pom.utils.CreditCardNumberMask
import com.amin.pom.utils.setCompoundDrawables
import com.shawnlin.numberpicker.NumberPicker
import io.card.payment.CardIOActivity
import io.card.payment.CreditCard
import kotlinx.android.synthetic.main.fragment_card.*


class CardFragment : Fragment() {

  private val cardViewModel: CardViewModel by viewModels()

  private lateinit var binding: FragmentCardBinding

  private var isExpireDialogOpened = false

  private val launchScanCard = registerForActivityResult(
    ActivityResultContracts.StartActivityForResult()
  ) { result ->
    if (result.data != null) {
      val scanResult: CreditCard = result.data!!.getParcelableExtra(
        CardIOActivity.EXTRA_SCAN_RESULT
      )!!
      cardViewModel.cardNumber.value = scanResult.formattedCardNumber

      if (scanResult.isExpiryValid)
        cardViewModel.setCardExpire(scanResult.expiryYear, scanResult.expiryMonth)
      else
        cardViewModel.clearExpire()

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

  override fun onSaveInstanceState(outState: Bundle) {
    super.onSaveInstanceState(outState)
    outState.putBoolean("IsExpireDialogOpened", isExpireDialogOpened)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    initCardNumberInputType()
    initObservers()

    isExpireDialogOpened = savedInstanceState?.getBoolean(
      "IsExpireDialogOpened", false
    ) ?: false

    binding.scanCard.setOnClickListener {
      val scanIntent = Intent(requireContext(), CardIOActivity::class.java).apply {
        putExtra(CardIOActivity.EXTRA_REQUIRE_EXPIRY, true)
        putExtra(CardIOActivity.EXTRA_REQUIRE_CVV, false)
      }

      launchScanCard.launch(scanIntent)
    }

    binding.cardExpire.setOnClickListener {
      showCardExpireDialog()
    }

    if (isExpireDialogOpened) {
      showCardExpireDialog()
    }
  }

  private fun initObservers() {
    cardViewModel.cardType.observe(viewLifecycleOwner) { type ->
      type?.let {
        val typeDrawableRes = when (type) {
          CardType.UNKNOWN -> 0
          CardType.VISA -> R.drawable.visa
          CardType.MASTERCARD -> R.drawable.mc
        }
        cardNumberInput.setCompoundDrawables(right = typeDrawableRes)
      }
    }
  }

  private fun initCardNumberInputType() {
    cardNumberInput.addTextChangedListener(CreditCardNumberMask())
  }

  private fun showCardExpireDialog() {
    var year: Int? = null
    var month: Int? = null
    cardViewModel.expireDate.value?.let { state ->
      if (state is CardViewModel.CardExpireState.CardExpire) {
        year = state.year
        month = state.month
      }
    }

    var yearPicker: NumberPicker? = null
    var monthPicker: NumberPicker? = null

    val dialog = AlertDialog.Builder(requireContext(), R.style.AlertDialogStyle).apply {
      setView(R.layout.dialog_card_expire)
      setNegativeButton(R.string.close) { dialogInterface: DialogInterface, _: Int ->
        dialogInterface.dismiss()
      }
      setPositiveButton(R.string.ok) { dialogInterface: DialogInterface, _: Int ->
        cardViewModel.setCardExpire(yearPicker!!.value, monthPicker!!.value)
        dialogInterface.dismiss()
      }

      setOnDismissListener {
        isExpireDialogOpened = false
      }
    }.create()

    isExpireDialogOpened = true
    dialog.show()

    yearPicker = dialog.findViewById(R.id.yearPicker)!!
    monthPicker = dialog.findViewById(R.id.monthPicker)!!

    if (year != null && year in yearPicker.minValue..yearPicker.maxValue)
      yearPicker.value = year!!

    if (month != null && month in 1..12)
      monthPicker.value = month!!
  }
}