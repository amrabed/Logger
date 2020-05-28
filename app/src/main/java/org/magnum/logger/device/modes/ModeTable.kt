package org.magnum.logger.device.modes

import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey

const val NAME = "Modes"

@Entity(tableName = NAME)
data class ModeRecord(@PrimaryKey val time: Long, val mode: Int)

@Dao
interface ModeTable {
    @Insert
    fun insert(vararg record: ModeRecord)
}