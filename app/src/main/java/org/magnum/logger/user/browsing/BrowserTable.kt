package org.magnum.logger.user.browsing

import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey

const val NAME = "Browser"

@Entity(tableName = NAME)
data class BrowserRecord(@PrimaryKey val time: Long, val address: String)

@Dao
interface BrowserTable {
    @Insert
    fun insert(vararg record: BrowserRecord)
}