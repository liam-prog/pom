package com.amin.pom.card

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.amin.pom.CardExpire
import com.amin.pom.CardType

class CardViewModel : ViewModel() {
  val cardNumber = MutableLiveData<String>()
  val cvv = MutableLiveData<String>()

  private val _expireDate = MutableLiveData<CardExpire>()
  val expireDate: LiveData<CardExpire> get() = _expireDate

  val cardExpire: LiveData<String> = Transformations.map(_expireDate) {
    it?.let { "${it.month}/${it.year}" }
  }

  fun setCardExpire(year: Int, month: Int) {
    _expireDate.value = CardExpire(year, month)
  }

  fun clearExpire() {
    _expireDate.value = null
  }

  val cardType: LiveData<CardType> = Transformations.map(cardNumber) {
    it?.trim()?.replace(" ", "")?.let(CardType::detect)
  }
}