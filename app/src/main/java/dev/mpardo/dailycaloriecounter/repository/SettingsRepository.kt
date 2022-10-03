package dev.mpardo.dailycaloriecounter.repository

import android.content.SharedPreferences
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject


private const val DailyCalorieGoalKey = "dev.mpardo.dailycaloriecoutner.DailyCalorieGoal"

class SharedPreferenceSettingsRepository : KoinComponent {
    private val sharedPreferences by inject<SharedPreferences>()
    
    var dailyCalorieGoal: Int
        get() = sharedPreferences.getInt(DailyCalorieGoalKey, 0)
        set(value) {
            sharedPreferences.edit().putInt(DailyCalorieGoalKey, value).apply()
        }
}