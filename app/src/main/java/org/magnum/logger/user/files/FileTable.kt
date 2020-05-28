package org.magnum.logger.user.files

import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey

const val NAME = "Files"

@Entity(tableName = NAME)
data class FileRecord(@PrimaryKey val time: Long, val fileId: String, val accessType: Int)

@Dao
interface FileTable {
    @Insert
    fun insert(vararg record: FileRecord)
}