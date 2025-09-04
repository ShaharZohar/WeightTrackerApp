package com.weighttrackerapp.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.weighttrackerapp.data.WeightEntry
import com.weighttrackerapp.database.AppDatabase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

// ViewModel to handle UI-related data and logic.
class WeightViewModel(application: Application) : ViewModel() {
    private val db = AppDatabase.getDatabase(application)
    private val weightDao = db.weightDao()

    val allWeightEntries: Flow<List<WeightEntry>> = weightDao.getAllWeightEntries()

    fun addWeightEntry(weight: Float, heightCm: Float) {
        viewModelScope.launch {
            val weightEntry = WeightEntry(weight = weight, heightCm = heightCm)
            weightDao.insert(weightEntry)
        }
    }

    fun clearAll() {
        viewModelScope.launch {
            weightDao.deleteAll()
        }
    }

    fun deleteEntry(id: Int) {
        viewModelScope.launch {
            weightDao.deleteById(id)
        }
    }
}

// ViewModel factory to create the ViewModel with an Application context.
class WeightViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WeightViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return WeightViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
