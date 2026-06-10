package com.app.rrq.data.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.app.rrq.data.local.SessionManager
import com.app.rrq.data.model.request.LoginRequest
import com.app.rrq.data.model.response.LoginResponse
import com.app.rrq.data.remote.retrofit.ApiClient
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class AuthRepository {

    private val apiService = ApiClient.apiService

    fun login(loginRequest: LoginRequest): LiveData<Resource<LoginResponse>> {
        val result = MutableLiveData<Resource<LoginResponse>>()
        result.postValue(Resource.Loading())

        apiService.login(loginRequest).enqueue(object : Callback<LoginResponse> {

            override fun onResponse(
                call: Call<LoginResponse>,
                response: Response<LoginResponse>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        result.postValue(Resource.Success(it))
                    } ?: run {
                        result.postValue(Resource.Error("Response kosong"))
                    }
                } else {
                    val errorMessage = try {
                        val errorJson = response.errorBody()?.string()
                        val jsonObject = JSONObject(errorJson)
                        jsonObject.getString("message")
                    } catch (e: Exception) {
                        "Username atau password salah"
                    }
                    result.postValue(Resource.Error(errorMessage))
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                result.postValue(Resource.Error(t.message ?: "Gagal terhubung ke server"))
            }
        })

        return result
    }

    fun register(
        nama: String,
        email: String,
        telepon: String,
        password: String,
        onResult: (Resource<Unit>) -> Unit
    ) {
        val auth = com.google.firebase.auth.FirebaseAuth.getInstance()
        val firestore = com.google.firebase.firestore.FirebaseFirestore.getInstance()

        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener { authResult ->
                val uid = authResult.user?.uid ?: return@addOnSuccessListener
                val userData = mapOf(
                    "nama" to nama,
                    "email" to email,
                    "telepon" to telepon,
                    "role" to "user"
                )
                firestore.collection("users").document(uid)
                    .set(userData)
                    .addOnSuccessListener {
                        onResult(Resource.Success(Unit))
                    }
                    .addOnFailureListener { e ->
                        onResult(Resource.Error(e.message ?: "Gagal menyimpan data"))
                    }
            }
            .addOnFailureListener { e ->
                onResult(Resource.Error(e.message ?: "Gagal mendaftar"))
            }
    }

    fun loginFirebase(
        context: Context,
        email: String,
        password: String,
        onResult: (Resource<String>) -> Unit
    ) {
        val auth = com.google.firebase.auth.FirebaseAuth.getInstance()
        val firestore = com.google.firebase.firestore.FirebaseFirestore.getInstance()
        val session = SessionManager(context)

        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener { authResult ->
                val uid = authResult.user?.uid ?: return@addOnSuccessListener
                firestore.collection("users").document(uid).get()
                    .addOnSuccessListener { doc ->
                        val role = doc.getString("role") ?: "user"
                        val nama = doc.getString("nama") ?: ""
                        val telepon = doc.getString("telepon") ?: ""
                        session.saveUser(nama, email, telepon, role, uid)
                        onResult(Resource.Success(role))
                    }
                    .addOnFailureListener { e ->
                        onResult(Resource.Error(e.message ?: "Gagal ambil data user"))
                    }
            }
            .addOnFailureListener { e ->
                onResult(Resource.Error(e.message ?: "Email atau password salah"))
            }
    }
}