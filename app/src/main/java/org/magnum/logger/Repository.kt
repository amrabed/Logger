package org.magnum.logger

import android.content.Context
import org.magnum.logger.communication.calls.CallRecord
import org.magnum.logger.communication.email.EmailRecord
import org.magnum.logger.communication.messaging.MessageRecord
import org.magnum.logger.connectivity.bluetooth.BluetoothRecord
import org.magnum.logger.connectivity.devices.DeviceRecord
import org.magnum.logger.connectivity.mobile.MobileRecord
import org.magnum.logger.connectivity.usb.UsbRecord
import org.magnum.logger.connectivity.wifi.WifiRecord
import org.magnum.logger.device.modes.ModeRecord
import org.magnum.logger.device.power.PowerRecord
import org.magnum.logger.user.applications.AppRecord
import org.magnum.logger.user.browsing.BrowserRecord
import org.magnum.logger.user.files.FileRecord
import org.magnum.logger.user.location.LocationRecord

class Repository(context: Context) {
    private val db = AppDatabase[context]

    fun logCall(callRecord: CallRecord) = AppDatabase.executor.execute { db?.callTable()?.insert(callRecord) }
    fun updateCall(callTime: Long, duration: Long) = AppDatabase.executor.execute { db?.callTable()?.setDuration(callTime, duration) }
    fun logMessage(messageRecord: MessageRecord) = AppDatabase.executor.execute { db?.messageTable()?.insert(messageRecord) }
    fun logEmail(emailRecord: EmailRecord) = AppDatabase.executor.execute { db?.emailTable()?.insert(emailRecord) }
    fun logBluetooth(bluetoothRecord: BluetoothRecord) = AppDatabase.executor.execute { db?.bluetoothTable()?.insert(bluetoothRecord) }
    fun logDevice(deviceRecord: DeviceRecord) = AppDatabase.executor.execute { db?.deviceTable()?.insert(deviceRecord) }
    fun logMobile(mobileRecord: MobileRecord) = AppDatabase.executor.execute { db?.mobileTable()?.insert(mobileRecord) }
    fun logUsb(usbRecord: UsbRecord) = AppDatabase.executor.execute { db?.usbTable()?.insert(usbRecord) }
    fun logWifi(wifiRecord: WifiRecord) = AppDatabase.executor.execute { db?.wifiTable()?.insert(wifiRecord) }
    fun logMode(modeRecord: ModeRecord) = AppDatabase.executor.execute { db?.modeTable()?.insert(modeRecord) }
    fun logPower(powerRecord: PowerRecord) = AppDatabase.executor.execute { db?.powerTable()?.insert(powerRecord) }
    fun logApp(appRecord: AppRecord) = AppDatabase.executor.execute { db?.appTable()?.insert(appRecord) }
    fun logFile(fileRecord: FileRecord) = AppDatabase.executor.execute { db?.fileTable()?.insert(fileRecord) }
    fun logLocation(locationRecord: LocationRecord) = AppDatabase.executor.execute { db?.locationTable()?.insert(locationRecord) }
    fun logBrowser(browserRecord: BrowserRecord) = AppDatabase.executor.execute { db?.browserTable()?.insert(browserRecord) }
}