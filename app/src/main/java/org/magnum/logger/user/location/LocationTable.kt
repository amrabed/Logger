package org.magnum.logger.user.location

import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey

const val NAME = "Location"

@Entity(tableName = NAME)
data class LocationRecord(@PrimaryKey val time: Long, val distance1: Float, val distance2: Float, val distance3: Float)

@Dao
interface LocationTable {
    @Insert
    fun insert(vararg record: LocationRecord)
}