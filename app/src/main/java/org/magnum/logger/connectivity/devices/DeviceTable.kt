package org.magnum.logger.connectivity.devices

import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey

const val NAME = "Devices"

@Entity(tableName = NAME)
data class DeviceRecord(@PrimaryKey val time: Long, val deviceId: String, val change: Int)

@Dao
interface DeviceTable {
    @Insert
    fun insert(vararg record: DeviceRecord)
}