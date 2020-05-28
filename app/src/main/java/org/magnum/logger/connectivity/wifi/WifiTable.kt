package org.magnum.logger.connectivity.wifi

import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey

const val NAME = "Wifi"

@Entity(tableName = NAME)
data class WifiRecord(@PrimaryKey val time: Long, val bssid: String)

@Dao
interface WifiTable {
    @Insert
    fun insert(vararg record: WifiRecord)
}