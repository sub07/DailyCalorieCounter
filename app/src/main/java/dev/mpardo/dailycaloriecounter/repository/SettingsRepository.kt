package dev.mpardo.dailycaloriecounter.repository

import android.content.SharedPreferences
import dev.mpardo.dailycaloriecounter.model.*
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject


private const val DailyCalorieGoalKey = "dev.mpardo.dailycaloriecounter.DailyCalorieGoal"
private const val DailyProteinGoalKey = "dev.mpardo.dailycaloriecounter.DailyProteinGoal"
private const val DailyFatsGoalKey = "dev.mpardo.dailycaloriecounter.DailyFatsGoal"
private const val DailySaltGoalKey = "dev.mpardo.dailycaloriecounter.DailySaltGoal"
private const val DailyCarbohydratesGoalKey = "dev.mpardo.dailycaloriecounter.DailyCarbohydratesGoal"

class SharedPreferenceSettingsRepository : KoinComponent {
    private val sharedPreferences by inject<SharedPreferences>()
    
    private fun put(key: String, v: Int) {
        sharedPreferences.edit().putInt(key, v).apply()
    }
    
    private fun get(key: String, default: Int = 0) = sharedPreferences.getInt(key, default)
    
    var dailyCalorieGoal: Energy
        get() = Energy(get(DailyCalorieGoalKey))
        set(energy) {
            put(DailyCalorieGoalKey, energy.value)
        }
    
    var dailyProteinGoal: Proteins
        get() = Proteins(get(DailyProteinGoalKey))
        set(protein) {
            put(DailyProteinGoalKey, protein.value)
        }
    
    var dailyFatsGoal: Fats
        get() = Fats(get(DailyFatsGoalKey))
        set(fats) {
            put(DailyFatsGoalKey, fats.value)
        }
    
    var dailySaltGoal: Salt
        get() = Salt(get(DailySaltGoalKey))
        set(salt) {
            put(DailySaltGoalKey, salt.value)
        }
    
    var dailyCarbohydratesGoal: Carbohydrates
        get() = Carbohydrates(get(DailyCarbohydratesGoalKey))
        set(carbohydrates) {
            put(DailyCarbohydratesGoalKey, carbohydrates.value)
        }
    
    var goals: Goals
        get() = Goals(dailyCalorieGoal, dailyProteinGoal, dailyFatsGoal, dailyCarbohydratesGoal, dailySaltGoal)
        set(goals) {
            dailySaltGoal = goals.salt
            dailyCarbohydratesGoal = goals.carbohydrates
            dailyFatsGoal = goals.fats
            dailyCalorieGoal = goals.energy
            dailyProteinGoal = goals.protein
        }
}