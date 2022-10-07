package dev.mpardo.dailycaloriecounter.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

sealed interface FoodCharacteristic : Parcelable

@Parcelize
class Energy(var value: Int) : FoodCharacteristic

@Parcelize
class Proteins(var value: Int) : FoodCharacteristic

@Parcelize
class Fats(var value: Int) : FoodCharacteristic

@Parcelize
class Salt(var value: Int) : FoodCharacteristic

@Parcelize
class Carbohydrates(var value: Int) : FoodCharacteristic
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
    override fun iterator(): Iterator<FoodCharacteristic> = listOf(energy, protein, fats, carbohydrates, salt).iterator()
    
}

@Parcelize
data class FoodEntryCounter(val food: FoodEntry, var count: Int) : Parcelable

@Parcelize
data class FoodRecord(val id: Long, var food: FoodEntry, var mass: Int, val date: Long) : Parcelable