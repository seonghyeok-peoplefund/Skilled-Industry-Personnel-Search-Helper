package com.ray.personnel.ui.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LoginViewModel : ViewModel() {
    val loginToken = MutableLiveData<String>()
    val client = MutableLiveData(
        LoginWebClient().also { client ->
            client.onTokenListener =
                fun(token: String) {
                    this.loginToken.value = token
                }
        }
    )
}