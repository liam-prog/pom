package com.amin.pom.utils

import android.content.Context
import android.content.DialogInterface
import androidx.appcompat.app.AlertDialog
import com.amin.pom.R
import com.shawnlin.numberpicker.NumberPicker

fun expireDateDialog(
  context: Context,
  year: Int? = null,
  month: Int? = null,
  onDateSet: (Int, Int) -> Unit
) {
  var yearPicker: NumberPicker? = null
  var monthPicker: NumberPicker? = null

  val dialog = AlertDialog.Builder(context, R.style.AlertDialogStyle).apply {
    setView(R.layout.dialog_card_expire)
    setNegativeButton(R.string.close) { dialogInterface: DialogInterface, _: Int ->
      dialogInterface.dismiss()
    }
    setPositiveButton(R.string.ok) { dialogInterface: DialogInterface, _: Int ->
      onDateSet(yearPicker!!.value, monthPicker!!.value)
      dialogInterface.dismiss()
    }
  }.create()

  dialog.show()

  yearPicker = dialog.findViewById(R.id.yearPicker)!!
  monthPicker = dialog.findViewById(R.id.monthPicker)!!

  if (year != null && year in yearPicker.minValue..yearPicker.maxValue)
    yearPicker.value = year

  if (month != null && month in 1..12)
    monthPicker.value = month
}