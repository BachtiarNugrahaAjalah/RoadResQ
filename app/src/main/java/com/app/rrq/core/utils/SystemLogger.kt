package com.app.rrq.core.utils

import android.util.Log
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object SystemLogger {
    private const val TAG = "RoadResQ_SystemLog"
    
    // In-memory list to keep track of logs (Mocking database for logs)
    private val logs = mutableListOf<String>()
    private val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())

    fun logActivity(action: String, userId: String = "unknown") {
        val timestamp = sdf.format(Date())
        val logMessage = "[$timestamp] User: $userId | Action: $action"
        
        // Print to logcat
        Log.d(TAG, logMessage)
        
        // Save to in-memory list
        logs.add(logMessage)
    }

    fun getLogs(): List<String> {
        return logs.toList()
    }
}
