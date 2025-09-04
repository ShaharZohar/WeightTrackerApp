package com.weighttrackerapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

// This data class represents a single weight entry in the database.
@Entity(tableName = "weight_entries")
data class WeightEntry(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val weight: Float,
    // Height in centimeters
    val heightCm: Float = 0f,
    val timestamp: Long = System.currentTimeMillis()
)
