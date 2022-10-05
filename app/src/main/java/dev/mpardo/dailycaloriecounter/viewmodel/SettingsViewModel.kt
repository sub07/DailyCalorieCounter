package dev.mpardo.dailycaloriecounter.viewmodel

import android.content.SharedPreferences
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.content.edit
import androidx.lifecycle.ViewModel
import dev.mpardo.dailycaloriecounter.repository.SharedPreferenceSettingsRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.component.inject

class SettingsViewModel : ViewModel(), KoinComponent {
    private val settingsRepository by inject<SharedPreferenceSettingsRepository>()
    
    var dailyCalorieGoal by mutableStateOf(0)
    var dailyProteinGoal by mutableStateOf(0)
    
    init {
        refresh()
    }
    
    fun setNewDailyCalorieGoal(newGoal: Int) {
        settingsRepository.dailyCalorieGoal = newGoal
        refresh()
    }
    
    fun setNewDailyProteinGoal(newGoal: Int) {
        settingsRepository.dailyProteinGoal = newGoal
        refresh()
    }
    
    fun setWelcomeDone() {
        get<SharedPreferences>().edit {
            putBoolean("showWelcomePage", false)
        }
    }
    
    fun refresh() {
        dailyCalorieGoal = settingsRepository.dailyCalorieGoal
        dailyProteinGoal = settingsRepository.dailyProteinGoal
    }
}