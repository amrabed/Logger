package org.magnum.logger.connectivity.usb

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.hardware.usb.UsbAccessory
import android.hardware.usb.UsbDevice
import android.hardware.usb.UsbManager
import android.os.Build
import android.util.Log
import org.magnum.logger.Encryptor
import org.magnum.logger.Repository

class UsbHandler : BroadcastReceiver() {
    @SuppressLint("DefaultLocale")
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
    override fun onReceive(context: Context, intent: Intent) {
        try {
            val time = System.currentTimeMillis()
            val action = intent.action
            if (action != null) {
                if (action.contains("UMS")) {
                    val description = action.substring(action.indexOf("UMS") + 4).toLowerCase()
                    Log.d(TAG, "UMS device connected/disconnected")
                    // TODO: not working correctly
                    val manager = context.getSystemService(Context.USB_SERVICE) as UsbManager
                    val devices = manager.deviceList
                    val keySet: Set<String> = devices.keys
                    for (key in keySet) {
                        val device = devices[key]
                        val name = Encryptor.encrypt(device!!.deviceName, context)
                        Repository(context).logUsb(UsbRecord(time, name, device.deviceClass.toString(), description))
                        Log.d(TAG, "UMS device connected/disconnected")
                    }
                } else {
                    val description = action.substring(action.indexOf("USB") + 4).toLowerCase()
                    if (description.contains("ACCESSORY")) {
                        val accessory = intent.getParcelableExtra<UsbAccessory>(UsbManager.EXTRA_ACCESSORY)
                        val id = Encryptor.encrypt(accessory?.serial, context)
                        Repository(context).logUsb(UsbRecord(time, id, accessory?.description
                                ?: "", description.substring(description.indexOf("ACCESSORY") + 10).toLowerCase()))
                    } else {
                        val device = intent.getParcelableExtra<UsbDevice>(UsbManager.EXTRA_DEVICE)
                        val name = Encryptor.encrypt(device?.deviceName, context)
                        Repository(context).logUsb(UsbRecord(time, name, device?.deviceClass.toString(), description.substring(description.indexOf("DEVICE") + 7).toLowerCase()))
                    }
                    Log.d(TAG, "USB device connected/disconnected")
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, e.toString())
        }
    }

    companion object {
        private val TAG = UsbHandler::class.java.canonicalName
    }
}