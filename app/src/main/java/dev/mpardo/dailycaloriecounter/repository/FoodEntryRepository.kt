package dev.mpardo.dailycaloriecounter.repository

import android.content.SharedPreferences
import dev.mpardo.dailycaloriecounter.model.FoodEntry
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

interface FoodEntryRepository : Repository<FoodEntry, Long>

private const val FoodEntryKey = "dev.mpardo.dailycaloriecoutner.FoodEntryJsonDb"
private const val FoodEntryNextIdKey = "dev.mpardo.dailycaloriecoutner.FoodEntryNextId"

class SharedPreferenceFoodEntryRepository : FoodEntryRepository, KoinComponent {
    
    private val sharedPreferences by inject<SharedPreferences>()
    
    @Serializable
    data class FoodEntryPref(
        val id: Long,
        var name: String,
        var calorieFor100g: Int
    ) {
        fun into() = FoodEntry(id, name, calorieFor100g)
        
        companion object {
            fun from(entry: FoodEntry) = FoodEntryPref(entry.id, entry.name, entry.calorieFor100g)
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