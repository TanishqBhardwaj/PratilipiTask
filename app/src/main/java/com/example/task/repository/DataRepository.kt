package com.example.task.repository

import com.example.task.local.Data
import com.example.task.local.Database

class DataRepository (
    private val db : Database
    ){

    suspend fun insert(data: Data) = db.dataDao().insertData(data)

    fun getAllData() = db.dataDao().getAllData()
}