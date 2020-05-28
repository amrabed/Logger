package org.magnum.logger.communication.messaging

import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey

const val NAME = "Messages"

@Entity(tableName = NAME)
data class MessageRecord(@PrimaryKey val time: Long, val number: String, val isReceived: Boolean)

@Dao
interface MessageTable {
    @Insert
    fun insert(vararg record: MessageRecord)
}