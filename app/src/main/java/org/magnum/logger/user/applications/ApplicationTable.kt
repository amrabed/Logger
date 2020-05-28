package org.magnum.logger.user.applications

import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey

const val NAME = "Applications"

@Entity(tableName = NAME)
data class AppRecord(@PrimaryKey val time: Long, val appId: String, val accessType: String)

@Dao
interface AppTable {
    @Insert
    fun insert(vararg record: AppRecord)
}