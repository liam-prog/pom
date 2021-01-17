package com.amin.pom.utils

import android.widget.EditText


fun EditText.setCompoundDrawables(left: Int = 0, top: Int = 0, right: Int = 0, bottom: Int = 0) {
  setCompoundDrawablesWithIntrinsicBounds(left, top, right, bottom)
}