package com.amin.pom.card

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CardViewModel : ViewModel() {

  private val _viewState = MutableLiveData<ViewState>(ViewState.Ready)
  val viewState: LiveData<ViewState> get() = _viewState

  val cardNumber = MutableLiveData<String>()
  val expireDate = MutableLiveData<String>()
  val cvv = MutableLiveData<String>()

  fun onExpireClicked() {
    var year: Int? = null
    var month: Int? = null
    expireDate.value?.let {
      it.split("/").let { list ->
        if (list.size == 2) {
          year = list[1].toIntOrNull()
          month = list[0].toIntOrNull()
        }
      }
    }

    _viewState.value = ViewState.OpenExpireDialog(year, month)
    _viewState.value = ViewState.Ready
  }

  sealed class ViewState {
    object Ready : ViewState()
    data class OpenExpireDialog(val year: Int?, val month: Int?) : ViewState()
  }
}