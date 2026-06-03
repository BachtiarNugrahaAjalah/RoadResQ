package com.app.rrq.model.data

/** Represents a user account stored in the JSON Gist */
data class UserAccount(
    val id: String,
    val name: String,
    val email: String,
    val password: String, // hashed password
    val phoneNumber: String,
    val role: String, // "Admin" or "User"
    val accountStatus: String, // e.g., "ACTIVE", "DISABLED"
    val createdAt: String // ISO date string
)
