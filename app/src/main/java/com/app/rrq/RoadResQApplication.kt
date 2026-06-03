package com.app.rrq

import android.app.Application
import com.app.rrq.core.session.SessionManager

class RoadResQApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        SessionManager.init(applicationContext)
    }
}
