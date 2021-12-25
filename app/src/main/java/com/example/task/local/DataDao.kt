package com.example.task.local

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface DataDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertData(data: Data)

    @Query("SELECT * FROM data_table")
    fun getAllData(): LiveData<List<Data>>

    @Update
    suspend fun updateData(data: Data)

    @Delete
    suspend fun deleteData(data: Data)
}