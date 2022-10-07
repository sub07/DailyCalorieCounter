package dev.mpardo.dailycaloriecounter.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dev.mpardo.dailycaloriecounter.model.*
import dev.mpardo.dailycaloriecounter.repository.SharedPreferenceSettingsRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class SettingsViewModel : ViewModel(), KoinComponent {
    private val settingsRepository by inject<SharedPreferenceSettingsRepository>()
    
    var dailyCalorieGoal by mutableStateOf(0)
    var dailyProteinGoal by mutableStateOf(0)
    var dailyFatsGoal by mutableStateOf(0)
    var dailyCarbohydratesGoal by mutableStateOf(0)
    var dailySaltGoal by mutableStateOf(0)
    
    val goals get() = settingsRepository.goals
    
    init {
        dailyCalorieGoal = settingsRepository.dailyCalorieGoal.value
        dailyProteinGoal = settingsRepository.dailyProteinGoal.value
        dailyFatsGoal = settingsRepository.dailyFatsGoal.value
        dailyCarbohydratesGoal = settingsRepository.dailyCarbohydratesGoal.value
        dailySaltGoal = settingsRepository.dailySaltGoal.value
    }
    
    fun setNewDailyCalorieGoal(newGoal: Energy) {
        settingsRepository.dailyCalorieGoal = newGoal
        dailyCalorieGoal = newGoal.value
    }
    
    fun setNewDailyProteinGoal(newGoal: Proteins) {
        settingsRepository.dailyProteinGoal = newGoal
        dailyProteinGoal = newGoal.value
    }
    
    fun setNewDailyFatsGoal(newGoal: Fats) {
        settingsRepository.dailyFatsGoal = newGoal
        dailyFatsGoal = newGoal.value
    }
    
    fun setNewDailyCarbohydratesGoal(newGoal: Carbohydrates) {
        settingsRepository.dailyCarbohydratesGoal = newGoal
        dailyCarbohydratesGoal = newGoal.value
    }
    
    fun setNewDailySaltGoal(newGoal: Salt) {
        settingsRepository.dailySaltGoal = newGoal
        dailySaltGoal = newGoal.value
    }
}