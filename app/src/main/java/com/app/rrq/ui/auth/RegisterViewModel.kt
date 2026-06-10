package com.app.rrq.ui.auth

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.app.rrq.data.repository.AuthRepository
import com.app.rrq.data.repository.Resource

class RegisterViewModel : ViewModel() {

    private val repository = AuthRepository()

    val registerState: MutableLiveData<Resource<Unit>> = MutableLiveData()

    fun register(nama: String, email: String, telepon: String, password: String) {
        registerState.postValue(Resource.Loading())
        repository.register(nama, email, telepon, password) { result ->
            registerState.postValue(result)
        }
    }
}