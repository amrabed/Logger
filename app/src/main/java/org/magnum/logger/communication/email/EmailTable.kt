package org.magnum.logger.communication.email

import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey

const val NAME = "Email"

@Entity(tableName = NAME)
data class EmailRecord(@PrimaryKey val time: Long, val address: String)

@Dao
interface EmailTable {
    @Insert
    fun insert(vararg record: EmailRecord)
}