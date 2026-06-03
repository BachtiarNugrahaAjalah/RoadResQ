package com.app.rrq.model.datasource

import com.app.rrq.model.data.UserAccount
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import java.net.HttpURLConnection
import java.net.URL

object GistUserDataSource {
    // Placeholder URL – replace with actual raw Gist URL when available
    private const val GIST_URL = "https://gist.githubusercontent.com/your-username/your-gist-id/raw/users.json"

    /**
     * Fetches the list of user accounts from the Gist JSON.
     * This runs on the IO dispatcher.
     */
    suspend fun fetchUsers(): List<UserAccount> = withContext(Dispatchers.IO) {
        val url = URL(GIST_URL)
        val connection = url.openConnection() as HttpURLConnection
        try {
            connection.requestMethod = "GET"
            connection.connectTimeout = 5000
            connection.readTimeout = 5000
            connection.doInput = true
            connection.connect()
            val responseCode = connection.responseCode
            if (responseCode != HttpURLConnection.HTTP_OK) {
                throw Exception("Failed to fetch users: HTTP $responseCode")
            }
            val inputStream = connection.inputStream
            val jsonText = inputStream.bufferedReader().use { it.readText() }
            parseUsersJson(jsonText)
        } finally {
            connection.disconnect()
        }
    }

    private fun parseUsersJson(json: String): List<UserAccount> {
        val jsonArray = JSONArray(json)
        val list = mutableListOf<UserAccount>()
        for (i in 0 until jsonArray.length()) {
            val obj = jsonArray.getJSONObject(i)
            val user = UserAccount(
                id = obj.getString("id"),
                name = obj.getString("name"),
                email = obj.getString("email"),
                password = obj.getString("password"), // assumed hashed
                phoneNumber = obj.optString("phoneNumber", ""),
                role = obj.getString("role"),
                accountStatus = obj.optString("accountStatus", "ACTIVE"),
                createdAt = obj.optString("createdAt", "")
            )
            list.add(user)
        }
        return list
    }
}
