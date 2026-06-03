package com.app.rrq.core.utils

import java.security.MessageDigest

object HashUtils {
    /**
     * Hashes string input with SHA-256 algorithm and returns the Hex string.
     */
    fun sha256(input: String): String {
        val bytes = input.toByteArray(Charsets.UTF_8)
        val md = MessageDigest.getInstance("SHA-256")
        val digest = md.digest(bytes)
        return digest.fold("") { str, it -> str + "%02x".format(it) }
    }
}
