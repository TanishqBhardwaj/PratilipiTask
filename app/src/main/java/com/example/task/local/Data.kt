package com.example.task.local

import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "data_table", indices = [Index(value = ["title"], unique = true) ])
data class Data(
    val title: String,
    val description : String,
    val image : String,

    @PrimaryKey(autoGenerate = true)
    var id : Int? = null
)
