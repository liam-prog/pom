package com.amin.pom.utils

import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher


class CreditCardNumberMask : TextWatcher {

  private val space = ' '

  override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit

  override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) = Unit

  override fun afterTextChanged(editable: Editable) {
    if (editable.isNotEmpty() && editable.length % 5 == 0) {
      val c: Char = editable.last()
      if (space == c) {
        editable.delete(editable.lastIndex, editable.length)
      }
    }
    if (editable.isNotEmpty() && editable.length % 5 == 0) {
      val c: Char = editable.last()
      // Only if its a digit where there should be a space we insert a space
      if (Character.isDigit(c) && TextUtils.split(
          editable.toString(),
          space.toString()
        ).size <= 3
      ) {
        editable.insert(editable.length - 1, space.toString())
      }
    }
  }
}