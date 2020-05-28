package org.magnum.logger.connectivity.bluetooth

import android.app.Service
import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.os.IBinder
import android.util.Log
import org.magnum.logger.Encryptor
import org.magnum.logger.Repository

class PairedDeviceReader : Service() {
    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        try {
            val time = System.currentTimeMillis()
            val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
            val devices = bluetoothAdapter.bondedDevices
            for (device in devices) {
                val name = Encryptor.encrypt(device.name, applicationContext)
                val address = Encryptor.encrypt(device.address, applicationContext)
                Repository(applicationContext).logBluetooth(BluetoothRecord(time, name, address, device.bluetoothClass.majorDeviceClass, "added"))
                Log.d(TAG, "Device " + device.address + " added at " + time)
            }
        } catch (e: Exception) {
            Log.e(TAG, e.toString())
        }
        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    companion object {
        private val TAG = PairedDeviceReader::class.java.canonicalName
    }
}