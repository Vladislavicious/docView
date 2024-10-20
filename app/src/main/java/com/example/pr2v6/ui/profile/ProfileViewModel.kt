package com.example.pr2v6.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ProfileViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "dj Ермак"
    }
    val text: LiveData<String> = _text

    private val _city = MutableLiveData<String>().apply {
        value = "Брянск"
    }
    val city: LiveData<String> = _city

    private val _email = MutableLiveData<String>().apply {
        value = "boba@email.cruk"
    }
    val email: LiveData<String> = _email

    private val _photoAddr = MutableLiveData<String>().apply {
        value = "https://avatars.mds.yandex.net/get-mpic/1985106/img_id2047960825122601198.jpeg/orig"
    }
    val photoAddr: LiveData<String> = _photoAddr

}