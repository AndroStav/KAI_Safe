package ua.androstav.kaisafe.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface InstructionDao {
    @Query("SELECT * FROM instructions")
    fun getAll(): LiveData<List<Instruction>>

    @Query("SELECT * FROM instructions")
    suspend fun getAllOnce(): List<Instruction>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(instructions: List<Instruction>)
}
