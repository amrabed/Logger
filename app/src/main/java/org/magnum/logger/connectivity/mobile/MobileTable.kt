package org.magnum.logger.connectivity.mobile

import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey

const val NAME = "Mobile"

@Entity(tableName = NAME)
data class MobileRecord(@PrimaryKey val time: Long, val cellId: String, val type: String?)

@Dao
interface MobileTable {
    @Insert
    fun insert(vararg record: MobileRecord)
}