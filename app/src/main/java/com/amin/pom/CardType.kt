package com.amin.pom

import java.util.regex.Pattern

enum class CardType {
  UNKNOWN,
  VISA("^4[0-9]{12}(?:[0-9]{3}){0,2}$"),
  MASTERCARD("^(?:5[1-5]|2(?!2([01]|20)|7(2[1-9]|3))[2-7])\\d{14}$");

  private var pattern: Pattern?

  constructor() {
    pattern = null
  }

  constructor(pattern: String) {
    this.pattern = Pattern.compile(pattern)
  }

  companion object {
    fun detect(cardNumber: String?): CardType {
      for (cardType in values()) {
        null == cardType.pattern && continue
        if (cardType.pattern!!.matcher(cardNumber).matches()) return cardType
      }
      return UNKNOWN
    }
  }
}