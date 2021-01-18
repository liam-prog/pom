package com.amin.pom.card

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.amin.pom.CardType

class CardViewModel : ViewModel() {
  val cardNumber = MutableLiveData<String>()
  val cvv = MutableLiveData<String>()

  private val _expireDate = MutableLiveData<CardExpireState>()
  val expireDate: LiveData<CardExpireState> get() = _expireDate

  val cardExpire: LiveData<String> = Transformations.map(_expireDate) {
    if (it is CardExpireState.CardExpire) {
      it.let { "${it.month}/${it.year}" }
    } else
      null
  }

  fun setCardExpire(year: Int, month: Int) {
    _expireDate.value = CardExpireState.CardExpire(year, month)
  }

  fun clearExpire() {
    _expireDate.value = CardExpireState.NotSet
  }

  val cardType: LiveData<CardType> = Transformations.map(cardNumber) {
    it?.trim()?.replace(" ", "")?.let(CardType::detect)
  }

  sealed class CardExpireState {
    data class CardExpire(val year: Int, val month: Int) : CardExpireState()
    object NotSet : CardExpireState()
  }
}