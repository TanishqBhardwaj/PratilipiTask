package com.example.task.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.task.local.Data
import com.example.task.repository.DataRepository
import kotlinx.coroutines.launch

class DataViewModel (
    private val dataRepository: DataRepository
    ) : ViewModel() {

    fun saveData(data : Data) = viewModelScope.launch {
        dataRepository.insert(data)
    }

    fun updateData(data: Data) = viewModelScope.launch {
        dataRepository.update(data)
    }

    fun deleteData(data: Data) = viewModelScope.launch {
        dataRepository.delete(data)
    }

    fun getAllData() = dataRepository.getAllData()
}