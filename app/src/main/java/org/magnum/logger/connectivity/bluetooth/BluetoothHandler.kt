package org.magnum.logger.connectivity.bluetooth

import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import org.magnum.logger.Encryptor
import org.magnum.logger.Repository

class BluetoothHandler : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        try {
            val time = System.currentTimeMillis()
            val action = intent.action
            val change = action!!.substring(action.lastIndexOf('.') + 1)
            val device = intent.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)
            if (device != null) {
                val name = Encryptor.encrypt(device.name, context)
                val address = Encryptor.encrypt(device.address, context)
                Repository(context).logBluetooth(BluetoothRecord(time, name, address, device.bluetoothClass.majorDeviceClass, change))
                Log.d(TAG, "Device " + device.address + " connected/disconnected at " + time)
            }
        } catch (e: Exception) {
            Log.e(TAG, e.toString())
        }
    }

    companion object {
        private val TAG = BluetoothHandler::class.java.canonicalName
    }
}