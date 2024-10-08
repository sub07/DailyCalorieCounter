package me.mpardo.dailycaloriecounter.repository

import android.content.SharedPreferences
import me.mpardo.dailycaloriecounter.model.Carbohydrates
import me.mpardo.dailycaloriecounter.model.Energy
import me.mpardo.dailycaloriecounter.model.Fats
import me.mpardo.dailycaloriecounter.model.Goals
import me.mpardo.dailycaloriecounter.model.Proteins
import me.mpardo.dailycaloriecounter.model.Salt
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject


private const val DailyCalorieGoalKey = "me.mpardo.dailycaloriecounter.DailyCalorieGoal"
private const val DailyProteinGoalKey = "me.mpardo.dailycaloriecounter.DailyProteinGoal"
private const val DailyFatsGoalKey = "me.mpardo.dailycaloriecounter.DailyFatsGoal"
private const val DailySaltGoalKey = "me.mpardo.dailycaloriecounter.DailySaltGoal"
private const val DailyCarbohydratesGoalKey = "me.mpardo.dailycaloriecounter.DailyCarbohydratesGoal"

class SharedPreferenceSettingsRepository : KoinComponent {
    private val sharedPreferences by inject<SharedPreferences>()

    private fun put(key: String, v: Float) {
        sharedPreferences.edit().putFloat(key, v).apply()
    }

    private fun get(key: String, default: Float = 1f) = sharedPreferences.getFloat(key, default)

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
        get() = Goals(
            dailyCalorieGoal,
            dailyProteinGoal,
            dailyFatsGoal,
            dailyCarbohydratesGoal,
            dailySaltGoal
        )
        set(goals) {
            dailySaltGoal = goals.salt
            dailyCarbohydratesGoal = goals.carbohydrates
            dailyFatsGoal = goals.fats
            dailyCalorieGoal = goals.energy
            dailyProteinGoal = goals.protein
        }
}