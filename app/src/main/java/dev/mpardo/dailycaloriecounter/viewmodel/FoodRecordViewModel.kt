package dev.mpardo.dailycaloriecounter.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dev.mpardo.dailycaloriecounter.model.FoodRecord
import dev.mpardo.dailycaloriecounter.repository.FoodRecordRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class FoodRecordViewModel : ViewModel(), KoinComponent {
    
    private val foodRecordRepository by inject<FoodRecordRepository>()
    
    var records by mutableStateOf(listOf<FoodRecord>())
        private set
    
    var totalCalorieRecorded by mutableStateOf(0)
        private set
    
    init {
        refresh()
    }
    
    fun addFoodEntry(e: FoodRecord) {
        foodRecordRepository.add(e)
        refresh()
    }
    
    fun deleteEntry(e: FoodRecord) {
        foodRecordRepository.delete(e)
        refresh()
    }
    
    fun deleteAll() {
        foodRecordRepository.deleteAll()
        refresh()
    }
    
    private fun refresh() {
        records = foodRecordRepository.all.sortedBy { it.date }
        totalCalorieRecorded = records.sumOf { it.mass * (it.food.calorieFor100g / 100.0) }.toInt()
    }
}