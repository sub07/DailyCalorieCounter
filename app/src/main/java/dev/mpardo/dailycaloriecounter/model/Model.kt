package dev.mpardo.dailycaloriecounter.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class FoodEntry(val id: Long, var name: String, var calorieFor100g: Int) : Parcelable

@Parcelize
data class FoodEntryCounter(val food: FoodEntry, var count: Int) : Parcelable

@Parcelize
data class FoodRecord(val id: Long, var food: FoodEntry, var mass: Int, val date: Long) : Parcelable