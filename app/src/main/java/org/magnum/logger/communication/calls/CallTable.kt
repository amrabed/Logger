package org.magnum.logger.communication.calls

import androidx.room.*

const val NAME = "calls"

@Entity(tableName = NAME)
data class CallRecord(@PrimaryKey val time: Long, val callId: String, val isIncoming: Boolean, val duration: Long?)

@Dao
interface CallTable {
    @Insert
    fun insert(vararg record: CallRecord)

    @Query("Update $NAME set duration = :duration where time = :time")
    fun setDuration(time: Long, duration: Long)
}