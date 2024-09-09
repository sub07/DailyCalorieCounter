package me.mpardo.dailycaloriecounter.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import me.mpardo.dailycaloriecounter.model.Carbohydrates
import me.mpardo.dailycaloriecounter.model.Energy
import me.mpardo.dailycaloriecounter.model.Fats
import me.mpardo.dailycaloriecounter.model.Proteins
import me.mpardo.dailycaloriecounter.model.Salt
import me.mpardo.dailycaloriecounter.repository.SharedPreferenceSettingsRepository
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
        dailyCalorieGoal = settingsRepository.dailyCalorieGoal.value.toInt()
        dailyProteinGoal = settingsRepository.dailyProteinGoal.value.toInt()
        dailyFatsGoal = settingsRepository.dailyFatsGoal.value.toInt()
        dailyCarbohydratesGoal = settingsRepository.dailyCarbohydratesGoal.value.toInt()
        dailySaltGoal = settingsRepository.dailySaltGoal.value.toInt()
    }

    fun setNewDailyCalorieGoal(newGoal: Energy) {
        settingsRepository.dailyCalorieGoal = newGoal
        dailyCalorieGoal = newGoal.value.toInt()
    }

    fun setNewDailyProteinGoal(newGoal: Proteins) {
        settingsRepository.dailyProteinGoal = newGoal
        dailyProteinGoal = newGoal.value.toInt()
    }

    fun setNewDailyFatsGoal(newGoal: Fats) {
        settingsRepository.dailyFatsGoal = newGoal
        dailyFatsGoal = newGoal.value.toInt()
    }

    fun setNewDailyCarbohydratesGoal(newGoal: Carbohydrates) {
        settingsRepository.dailyCarbohydratesGoal = newGoal
        dailyCarbohydratesGoal = newGoal.value.toInt()
    }

    fun setNewDailySaltGoal(newGoal: Salt) {
        settingsRepository.dailySaltGoal = newGoal
        dailySaltGoal = newGoal.value.toInt()
    }
}