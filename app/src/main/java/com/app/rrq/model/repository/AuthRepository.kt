package com.app.rrq.model.repository

import com.app.rrq.core.utils.SystemLogger
import com.app.rrq.model.data.LoginResult
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AuthRepository(
    private val auth: FirebaseAuth = FirebaseAuth.getInstance(),
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
) {
    suspend fun login(email: String, password: String): LoginResult = withContext(Dispatchers.IO) {
        val authResult = Tasks.await(auth.signInWithEmailAndPassword(email.trim(), password))
        val firebaseUser = authResult.user ?: throw Exception("User Firebase tidak ditemukan")
        val userDoc = Tasks.await(db.collection(USERS).document(firebaseUser.uid).get())

        if (!userDoc.exists()) {
            auth.signOut()
            throw Exception("Profil pengguna belum tersedia di Firestore")
        }

        val accountStatus = userDoc.getString("accountStatus") ?: ACCOUNT_ACTIVE
        if (accountStatus != ACCOUNT_ACTIVE) {
            auth.signOut()
            throw Exception("Akun Anda sedang dinonaktifkan")
        }

        Tasks.await(
            db.collection(USERS).document(firebaseUser.uid).update(
                mapOf(
                    "lastLoginAt" to FieldValue.serverTimestamp(),
                    "updatedAt" to FieldValue.serverTimestamp()
                )
            )
        )

        SystemLogger.logActivity("${userDoc.roleLabel()} Logged In Successfully", email)
        userDoc.toLoginResult(firebaseUser.uid)
    }

    suspend fun register(name: String, email: String, password: String): LoginResult = withContext(Dispatchers.IO) {
        register(name, email, password, "")
    }

    suspend fun register(name: String, email: String, password: String, phoneNumber: String): LoginResult = withContext(Dispatchers.IO) {
        val cleanEmail = email.trim()
        if (name.isBlank() || !cleanEmail.contains("@") || password.length < 6) {
            throw Exception("Data registrasi tidak valid")
        }

        val authResult = Tasks.await(auth.createUserWithEmailAndPassword(cleanEmail, password))
        val firebaseUser = authResult.user ?: throw Exception("User Firebase tidak ditemukan")
        val userData = mapOf(
            "uid" to firebaseUser.uid,
            "fullName" to name.trim(),
            "email" to cleanEmail,
            "emailLower" to cleanEmail.lowercase(),
            "phoneNumber" to phoneNumber.trim(),
            "role" to ROLE_USER,
            "accountStatus" to ACCOUNT_ACTIVE,
            "photoUrl" to null,
            "createdAt" to FieldValue.serverTimestamp(),
            "updatedAt" to FieldValue.serverTimestamp(),
            "lastLoginAt" to FieldValue.serverTimestamp()
        )

        Tasks.await(db.collection(USERS).document(firebaseUser.uid).set(userData))
        SystemLogger.logActivity("New User Registered", cleanEmail)

        LoginResult(
            userId = firebaseUser.uid,
            email = cleanEmail,
            name = name.trim(),
            role = roleToUiLabel(ROLE_USER)
        )
    }

    suspend fun getCurrentUserSession(): LoginResult? = withContext(Dispatchers.IO) {
        val firebaseUser = auth.currentUser ?: return@withContext null
        val userDoc = Tasks.await(db.collection(USERS).document(firebaseUser.uid).get())

        if (!userDoc.exists()) {
            auth.signOut()
            return@withContext null
        }

        val accountStatus = userDoc.getString("accountStatus") ?: ACCOUNT_ACTIVE
        if (accountStatus != ACCOUNT_ACTIVE) {
            auth.signOut()
            return@withContext null
        }

        userDoc.toLoginResult(firebaseUser.uid)
    }

    fun logout() {
        auth.signOut()
    }

    private fun DocumentSnapshot.toLoginResult(uid: String): LoginResult {
        return LoginResult(
            userId = uid,
            email = getString("email") ?: "",
            name = getString("fullName") ?: "",
            role = roleLabel()
        )
    }

    private fun DocumentSnapshot.roleLabel(): String {
        return roleToUiLabel(getString("role") ?: ROLE_USER)
    }

    private fun roleToUiLabel(role: String): String {
        return when (role.uppercase()) {
            ROLE_ADMIN -> "Admin"
            else -> "User"
        }
    }

    private companion object {
        const val USERS = "users"
        const val ROLE_ADMIN = "ADMIN"
        const val ROLE_USER = "USER"
        const val ACCOUNT_ACTIVE = "ACTIVE"
    }
}
