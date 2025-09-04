package com.weighttrackerapp.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.weighttrackerapp.data.WeightEntry
import kotlinx.coroutines.flow.Flow

// Data Access Object (DAO) for interacting with the weight_entries table.
@Dao
interface WeightDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(weightEntry: WeightEntry)

    @Query("SELECT * FROM weight_entries ORDER BY timestamp ASC")
    fun getAllWeightEntries(): Flow<List<WeightEntry>>

    @Query("DELETE FROM weight_entries")
    suspend fun deleteAll()

    @Query("DELETE FROM weight_entries WHERE id = :id")
    suspend fun deleteById(id: Int)
}
