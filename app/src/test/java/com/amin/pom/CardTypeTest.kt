package com.amin.pom

import org.junit.Assert.assertEquals
import org.junit.Test

class CardTypeTest {

  @Test
  fun `detect_it should return unknown for invalid card number`() {
    assertEquals(CardType.UNKNOWN, CardType.detect("1111111111111111"))
  }

  @Test
  fun `detect_it should return visa card for 4000123456789010`() {
    assertEquals(CardType.VISA, CardType.detect("4000123456789010"))
  }

  @Test
  fun `detect_it should return master card for 5412751232123456`() {
    assertEquals(CardType.MASTERCARD, CardType.detect("5412751232123456"))
  }
}