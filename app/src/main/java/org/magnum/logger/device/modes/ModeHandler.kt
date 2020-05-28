package org.magnum.logger.device.modes

import android.app.UiModeManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.util.Log
import org.magnum.logger.Repository

class ModeHandler : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        try {
            val time = System.currentTimeMillis()
            when (intent.action) {
                Intent.ACTION_AIRPLANE_MODE_CHANGED -> {
                    if (intent.getBooleanExtra("state", false)) {
                        Repository(context).logMode(ModeRecord(time, 4))
                        Log.d(TAG, "Airplane Mode activated at $time")
                    } else {
                        Repository(context).logMode(ModeRecord(time, 3))
                        Log.d(TAG, "Airplane Mode deactivated at $time")
                    }
                }
                Intent.ACTION_DOCK_EVENT -> {
                    val uiMode = context.getSystemService(Context.UI_MODE_SERVICE) as UiModeManager
                    Repository(context).logMode(ModeRecord(time, uiMode.currentModeType + 4))
                    Log.d(TAG, "UI mode changed at $time")
                }
                else -> {
                    Repository(context).logMode(ModeRecord(time, intent.getIntExtra(AudioManager.EXTRA_RINGER_MODE, -1)))
                    Log.d(TAG, "Ringer mode changed at $time")
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, e.toString())
        }
    }

    companion object {
        private val TAG = ModeHandler::class.java.canonicalName
    }
}