package me.mpardo.dailycaloriecounter.repository

import android.content.SharedPreferences
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import me.mpardo.dailycaloriecounter.model.Carbohydrates
import me.mpardo.dailycaloriecounter.model.Energy
import me.mpardo.dailycaloriecounter.model.Fats
import me.mpardo.dailycaloriecounter.model.FoodEntry
import me.mpardo.dailycaloriecounter.model.Proteins
import me.mpardo.dailycaloriecounter.model.Salt
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

interface FoodEntryRepository : Repository<FoodEntry, Long>

private const val FoodEntryKey = "me.mpardo.dailycaloriecoutner.FoodEntryJsonDb"
private const val FoodEntryNextIdKey = "me.mpardo.dailycaloriecoutner.FoodEntryNextId"

class SharedPreferenceFoodEntryRepository : FoodEntryRepository, KoinComponent {

    private val sharedPreferences by inject<SharedPreferences>()

    @Serializable
    data class FoodEntryPref(
        val id: Long,
        var name: String,
        var energy: Float,
        var protein: Float,
        var fats: Float,
        var salt: Float,
        var carbohydrates: Float,
    ) {
        fun into() = FoodEntry(
            id,
            name,
            Energy(energy),
            Proteins(protein),
            Fats(fats),
            Carbohydrates(carbohydrates),
            Salt(salt)
        )

        companion object {
            fun from(entry: FoodEntry) = FoodEntryPref(
                entry.id,
                entry.name,
                entry.energy.value,
                entry.proteins.value,
                entry.fats.value,
                entry.salt.value,
                entry.carbohydrates.value
            )
        }
    }

    private fun FoodEntry.into(id: Long = this.id) = FoodEntryPref.from(this).copy(id = id)

    private var json
        get() = sharedPreferences.getString(FoodEntryKey, "[]") ?: "[]"
        set(value) {
            sharedPreferences.edit().putString(FoodEntryKey, value).apply()
        }

    private val allPref: List<FoodEntryPref> get() = Json.decodeFromString(json)

    override val nextId: Long
        get() {
            val nextId = sharedPreferences.getLong(FoodEntryNextIdKey, 0)
            sharedPreferences.edit().putLong(FoodEntryNextIdKey, nextId + 1).apply()
            return nextId
        }

    override val all: List<FoodEntry>
        get() = allPref.map { it.into() }

    override fun get(key: Long): FoodEntry? = allPref.find { it.id == key }?.into()

    override fun add(e: FoodEntry) {
        json = Json.encodeToString(allPref + e.into(id = nextId))
    }

    override fun delete(e: FoodEntry) {
        json = Json.encodeToString(allPref - e.into())
    }

    override fun deleteAll() {
        json = "[]"
    }

    override fun update(e: FoodEntry) {
        json = Json.encodeToString(allPref.map { if (it.id == e.id) e.into() else it })
    }
}