package com.example.pr2v6.ui.shoppingCart

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class shoppingCartViewModel : ViewModel() {
    private val _noItems = MutableLiveData<String>().apply {
        value = "Нет записей"
    }
    val noItems: LiveData<String> = _noItems

    private val _buttonName = MutableLiveData<String>().apply {
        value = "Оформить записи"
    }
    val buttonName: LiveData<String> = _buttonName


    private val _declineButtonName = MutableLiveData<String>().apply {
        value = "Отменить записи"
    }
    val declineButtonName: LiveData<String> = _declineButtonName
}