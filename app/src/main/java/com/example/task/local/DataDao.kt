package com.example.task.local

import androidx.lifecycle.LiveData
import androidx.paging.PagingSource
import androidx.room.*

@Dao
interface DataDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertData(data: Data)

    @Query("SELECT * FROM data_table ORDER BY id DESC")
    fun getAllData(): PagingSource<Int, Data>

    @Update
    suspend fun updateData(data: Data)

    @Delete
    suspend fun deleteData(data: Data)
}