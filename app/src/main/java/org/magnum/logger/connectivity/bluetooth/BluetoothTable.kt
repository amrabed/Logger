package org.magnum.logger.connectivity.bluetooth

import androidx.room.*

const val NAME = "Bluetooth"

@Entity(tableName = NAME)
data class BluetoothRecord(@PrimaryKey val time: Long, val name: String, val address: String, @ColumnInfo(name = "class") val clazz: Int, val change: String)

@Dao
interface BluetoothTable {
    @Insert
    fun insert(vararg record: BluetoothRecord)
}
