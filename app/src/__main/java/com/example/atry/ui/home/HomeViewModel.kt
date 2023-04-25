package com.example.atry.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
//    val pathList: MutableLiveData<String> by lazy {
//        MutableLiveData<String>()
//    }
    val text: LiveData<String> = _text
}