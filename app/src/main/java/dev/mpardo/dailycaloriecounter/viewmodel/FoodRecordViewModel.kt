package dev.mpardo.dailycaloriecounter.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dev.mpardo.dailycaloriecounter.model.FoodRecord
import dev.mpardo.dailycaloriecounter.repository.FoodRecordRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.time.Instant

class FoodRecordViewModel : ViewModel(), KoinComponent {
    
    private val foodRecordRepository by inject<FoodRecordRepository>()
    
    var records by mutableStateOf(listOf<FoodRecord>())
        private set
    
    var totalCalorieRecorded by mutableStateOf(0)
        private set
    
    var totalProteinRecorded by mutableStateOf(0)
        private set
    
    var totalFatsRecorded by mutableStateOf(0)
        private set
    
    var totalCarbohydratesRecorded by mutableStateOf(0)
        private set
    
    var totalSaltRecorded by mutableStateOf(0)
        private set
    
    var nbHourSinceFirstMeal by mutableStateOf(-1)
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
        clearCustomEnergyFood()
        refresh()
    }
    
    fun addCustomEnergyRecord(amount: Int) {
        foodRecordRepository.addCustomEnergyRecord(amount)
        refresh()
    }
    
    fun clearCustomEnergyFood() {
        foodRecordRepository.clearCustomEnergyRecord()
        refresh()
    }
    
    private fun refresh() {
        records = foodRecordRepository.all.sortedBy { it.date }
        totalCalorieRecorded = records.sumOf { it.mass * (it.food.energy.value / 100.0) }.toInt() + foodRecordRepository.customEnergyAmount
        totalProteinRecorded = records.sumOf { it.mass * (it.food.proteins.value / 100.0) }.toInt()
        totalFatsRecorded = records.sumOf { it.mass * (it.food.fats.value / 100.0) }.toInt()
        totalCarbohydratesRecorded = records.sumOf { it.mass * (it.food.carbohydrates.value / 100.0) }.toInt()
        totalSaltRecorded = records.sumOf { it.mass * (it.food.salt.value / 100.0) }.toInt()
        nbHourSinceFirstMeal = if (records.firstOrNull() == null) {
            -1
        } else {
            (Instant.now().epochSecond - records.firstOrNull()!!.date).toInt() / 3600
        }
    }
}