package me.mpardo.dailycaloriecounter.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

sealed interface FoodCharacteristic : Parcelable

@Parcelize
data class Energy(var value: Float) : FoodCharacteristic

@Parcelize
data class Proteins(var value: Float) : FoodCharacteristic

@Parcelize
data class Fats(var value: Float) : FoodCharacteristic

@Parcelize
data class Salt(var value: Float) : FoodCharacteristic

@Parcelize
data class Carbohydrates(var value: Float) : FoodCharacteristic

@Parcelize
data class FoodEntry(
    val id: Long,
    var name: String,
    val energy: Energy,
    val proteins: Proteins,
    val fats: Fats,
    val carbohydrates: Carbohydrates,
    val salt: Salt,
) : Parcelable

data class Goals(
    val energy: Energy,
    val protein: Proteins,
    val fats: Fats,
    val carbohydrates: Carbohydrates,
    val salt: Salt,
) : Iterable<FoodCharacteristic> {
    override fun iterator(): Iterator<FoodCharacteristic> =
        listOf(energy, protein, fats, carbohydrates, salt).iterator()

}

@Parcelize
data class FoodEntryCounter(val food: FoodEntry, var count: Int) : Parcelable

@Parcelize
data class FoodRecord(val id: Long, var food: FoodEntry, var mass: Int, val date: Long) : Parcelable