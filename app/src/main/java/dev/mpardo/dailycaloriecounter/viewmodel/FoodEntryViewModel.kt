package dev.mpardo.dailycaloriecounter.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dev.mpardo.dailycaloriecounter.model.FoodEntry
import dev.mpardo.dailycaloriecounter.model.FoodEntryCounter
import dev.mpardo.dailycaloriecounter.repository.FoodEntryCounterRepository
import dev.mpardo.dailycaloriecounter.repository.FoodEntryRepository
import dev.mpardo.dailycaloriecounter.repository.InMemoryFoodEntryCounterRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class FoodEntryViewModel : ViewModel(), KoinComponent {
    
    private val foodEntryRepository by inject<FoodEntryRepository>()
    private val foodEntryCounterRepository by inject<FoodEntryCounterRepository>()
    
    var calDb by mutableStateOf(listOf<FoodEntry>())
        private set
    
    var foodDbCounter by mutableStateOf(mapOf<FoodEntry, Int>())
    
    init {
        refreshAll()
    }
    
    fun addFoodEntry(e: FoodEntry) {
        foodEntryRepository.add(e)
        refreshAll()
    }
    
    fun deleteEntry(e: FoodEntry) {
        foodEntryRepository.delete(e)
        refreshAll()
    }
    
    fun updateEntry(e: FoodEntry) {
        foodEntryRepository.update(e)
        refreshAll()
    }
    
    fun addOneFoodEntryUse(e: FoodEntry) {
        val value = foodEntryCounterRepository.get(e)
        value?.let {
            foodEntryCounterRepository.update(it.copy(count = it.count + 1))
        }
        refreshCounter()
    }
    
    private fun refreshAll() {
        calDb = foodEntryRepository.all.sortedBy { it.name }
        refreshCounter()
    }
    
    private fun refreshCounter() {
        foodDbCounter = calDb.map { foodEntryCounterRepository.get(it) ?: FoodEntryCounter(it, 0) }.associate { it.food to it.count }
    }
}