package org.magnum.logger

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import org.magnum.logger.communication.calls.CallRecord
import org.magnum.logger.communication.calls.CallTable
import org.magnum.logger.communication.email.EmailRecord
import org.magnum.logger.communication.email.EmailTable
import org.magnum.logger.communication.messaging.MessageRecord
import org.magnum.logger.communication.messaging.MessageTable
import org.magnum.logger.connectivity.bluetooth.BluetoothRecord
import org.magnum.logger.connectivity.bluetooth.BluetoothTable
import org.magnum.logger.connectivity.devices.DeviceRecord
import org.magnum.logger.connectivity.devices.DeviceTable
import org.magnum.logger.connectivity.mobile.MobileRecord
import org.magnum.logger.connectivity.mobile.MobileTable
import org.magnum.logger.connectivity.usb.UsbRecord
import org.magnum.logger.connectivity.usb.UsbTable
import org.magnum.logger.connectivity.wifi.WifiRecord
import org.magnum.logger.connectivity.wifi.WifiTable
import org.magnum.logger.device.modes.ModeRecord
import org.magnum.logger.device.modes.ModeTable
import org.magnum.logger.device.power.PowerRecord
import org.magnum.logger.device.power.PowerTable
import org.magnum.logger.user.applications.AppRecord
import org.magnum.logger.user.applications.AppTable
import org.magnum.logger.user.browsing.BrowserRecord
import org.magnum.logger.user.browsing.BrowserTable
import org.magnum.logger.user.files.FileRecord
import org.magnum.logger.user.files.FileTable
import org.magnum.logger.user.location.LocationRecord
import org.magnum.logger.user.location.LocationTable
import java.util.concurrent.Executor
import java.util.concurrent.Executors

@Database(entities = [AppRecord::class, BluetoothRecord::class, BrowserRecord::class, CallRecord::class, DeviceRecord::class, EmailRecord::class, FileRecord::class, LocationRecord::class, MessageRecord::class, MobileRecord::class, ModeRecord::class, PowerRecord::class, UsbRecord::class, WifiRecord::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun appTable(): AppTable
    abstract fun bluetoothTable(): BluetoothTable
    abstract fun browserTable(): BrowserTable
    abstract fun callTable(): CallTable
    abstract fun deviceTable(): DeviceTable
    abstract fun emailTable(): EmailTable
    abstract fun fileTable(): FileTable
    abstract fun locationTable(): LocationTable
    abstract fun messageTable(): MessageTable
    abstract fun mobileTable(): MobileTable
    abstract fun modeTable(): ModeTable
    abstract fun powerTable(): PowerTable
    abstract fun usbTable(): UsbTable
    abstract fun wifiTable(): WifiTable

    companion object {
        @Volatile
        private var database: AppDatabase? = null
        val executor: Executor = Executors.newFixedThreadPool(4)

        operator fun get(context: Context): AppDatabase? {
            synchronized(AppDatabase::class.java) {
                if (database == null) {
                    database = Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, "log").build()
                }
            }
            return database
        }
    }
}