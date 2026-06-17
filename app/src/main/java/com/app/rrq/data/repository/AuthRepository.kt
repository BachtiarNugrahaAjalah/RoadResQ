package com.app.rrq.data.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.app.rrq.data.local.SessionManager
import com.app.rrq.data.model.request.LoginRequest
import com.app.rrq.data.model.response.LoginResponse
import com.app.rrq.data.remote.retrofit.ApiClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class AuthRepository {

    private val apiService = ApiClient.apiService

    private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    private val firestore: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }

    fun login(loginRequest: LoginRequest): LiveData<Resource<LoginResponse>> {
        val result = MutableLiveData<Resource<LoginResponse>>()
        result.postValue(Resource.Loading())

        apiService.login(loginRequest).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    response.body()?.let { result.postValue(Resource.Success(it)) }
                        ?: result.postValue(Resource.Error("Response kosong"))
                } else {
                    val errorMessage = try {
                        val errorJson = response.errorBody()?.string()
                        JSONObject(errorJson).getString("message")
                    } catch (e: Exception) { "Username atau password salah" }
                    result.postValue(Resource.Error(errorMessage))
                }
            }
            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                result.postValue(Resource.Error(t.message ?: "Gagal terhubung ke server"))
            }
        })
        return result
    }

    fun loginFirebase(
        context: Context,
        email: String,
        password: String,
        onResult: (Resource<String>) -> Unit
    ) {
        val session = SessionManager(context)

        // Firebase Auth login
        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener { authResult ->
                val uid = authResult.user?.uid ?: return@addOnSuccessListener
                
                // Ambil data dari Firestore
                firestore.collection("users").document(uid).get()
                    .addOnSuccessListener { doc ->
                        if (doc.exists()) {
                            val status = doc.getString("status") ?: "AKTIF"
                            
                            if (status == "BANNED") {
                                auth.signOut() // Logout user karena diblokir
                                onResult(Resource.Error("Akun Anda telah di-banned. Silakan hubungi admin."))
                                return@addOnSuccessListener
                            }

                            val role = doc.getString("role") ?: "user"
                            val nama = doc.getString("name") ?: doc.getString("nama") ?: ""
                            val telepon = doc.getString("phone") ?: doc.getString("telepon") ?: ""
                            val photoUrl = doc.getString("photoUrl") ?: ""
                            
                            session.saveUser(nama, email, telepon, role.lowercase(), uid)
                            session.savePhotoUrl(photoUrl)
                            onResult(Resource.Success(role.lowercase()))
                        } else {
                            auth.signOut() // Logout user jika data tidak ada (terhapus)
                            onResult(Resource.Error("Akun tidak ditemukan atau telah dihapus."))
                        }
                    }
                    .addOnFailureListener { e ->
                        onResult(Resource.Error(e.message ?: "Gagal mengambil data profil"))
                    }
            }
            .addOnFailureListener { e ->
                onResult(Resource.Error("Email atau password salah"))
            }
    }

    fun register(
        nama: String,
        email: String,
        telepon: String,
        password: String,
        onResult: (Resource<Unit>) -> Unit
    ) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener { authResult ->
                val uid = authResult.user?.uid ?: return@addOnSuccessListener
                
                val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                val currentDate = dateFormat.format(Date())

                val userData = mapOf(
                    "userId" to uid,
                    "name" to nama,
                    "email" to email,
                    "phone" to telepon,
                    "role" to "USER",
                    "status" to "AKTIF",
                    "date" to currentDate,
                    "photoUrl" to ""
                )
                
                firestore.collection("users").document(uid)
                    .set(userData)
                    .addOnSuccessListener { onResult(Resource.Success(Unit)) }
                    .addOnFailureListener { e -> onResult(Resource.Error(e.message ?: "Gagal menyimpan data")) }
            }
            .addOnFailureListener { e ->
                onResult(Resource.Error(e.message ?: "Gagal mendaftar"))
            }
    }
}
