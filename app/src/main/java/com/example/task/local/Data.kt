package com.example.task.local

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "data_table", indices = [Index(value = ["id"], unique = true) ])
data class Data(
    val title: String,
    val description : String,
    val image : String,

    @PrimaryKey(autoGenerate = true)
    var id : Int?=null
) : Serializable
