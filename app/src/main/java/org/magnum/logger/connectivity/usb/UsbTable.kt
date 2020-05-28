package org.magnum.logger.connectivity.usb

import androidx.room.*

const val NAME = "Usb"

@Entity(tableName = NAME)
data class UsbRecord(@PrimaryKey val time: Long, val name: String, @ColumnInfo(name = "class") val clazz: String, val change: String)

@Dao
interface UsbTable {
    @Insert
    fun insert(vararg record: UsbRecord)
}