package com.app.rrq.model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.app.rrq.data.repository.AuthRepository
import com.app.rrq.data.repository.Resource

class LoginViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = AuthRepository()

    val loginState: MutableLiveData<Resource<String>> = MutableLiveData()

    fun login(email: String, password: String) {
        loginState.value = Resource.Loading()
        
        repository.loginFirebase(getApplication(), email, password) { result ->
            loginState.value = result
        }
    }
}