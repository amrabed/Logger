package org.magnum.logger.device.power

import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey

const val NAME = "Power"

@Entity(tableName = NAME)
data class PowerRecord(@PrimaryKey val time: Long, val status: Int, val level: Float, val isPlugged: Boolean)

@Dao
interface PowerTable {
    @Insert
    fun insert(vararg record: PowerRecord)
}