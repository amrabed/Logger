package org.magnum.logger

import android.app.Service
import android.content.Intent
import android.os.IBinder

class SyncService : Service() {
    override fun onBind(intent: Intent): IBinder? {
        return null
    }
}