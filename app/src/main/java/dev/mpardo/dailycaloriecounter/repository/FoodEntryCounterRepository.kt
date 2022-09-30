package dev.mpardo.dailycaloriecounter.repository

import android.content.SharedPreferences
import dev.mpardo.dailycaloriecounter.model.FoodEntry
import dev.mpardo.dailycaloriecounter.model.FoodEntryCounter
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

interface FoodEntryCounterRepository : Repository<FoodEntryCounter, FoodEntry>

private const val FoodEntryCounterKey = "dev.mpardo.dailycaloriecoutner.FoodEntryCounterJsonDb"

class SharedPreferenceFoodEntryCounterRepository : FoodEntryCounterRepository, KoinComponent {
    
    private val sharedPreferences by inject<SharedPreferences>()
    private val foodEntryRepository by inject<FoodEntryRepository>()
    
    @Serializable
    data class FoodEntryCounterPref(
        val foodEntryId: Long, var counter: Int
    ) {
        fun into(foodEntryRepository: FoodEntryRepository) = FoodEntryCounter(foodEntryRepository.get(foodEntryId)!!, counter)
        
        companion object {
            fun from(entry: FoodEntryCounter) = FoodEntryCounterPref(entry.food.id, entry.count)
        }
    }
    
    private fun FoodEntryCounter.into() = FoodEntryCounterPref.from(this)
    
    private var json
        get() = sharedPreferences.getString(FoodEntryCounterKey, "[]") ?: "[]"
        set(value) {
            sharedPreferences.edit().putString(FoodEntryCounterKey, value).apply()
        }
    
    private val allPref: List<FoodEntryCounterPref> get() = Json.decodeFromString(json)
    
    override val nextId: Long
        get() = error("Should not be used")
    
    override val all: List<FoodEntryCounter>
        get() = allPref.map { it.into(foodEntryRepository) }
    
    override fun get(key: FoodEntry): FoodEntryCounter? = allPref.find { it.foodEntryId == key.id }?.into(foodEntryRepository)
    
    override fun add(e: FoodEntryCounter) {
        json = Json.encodeToString(allPref + e.into())
    }
    
    override fun delete(e: FoodEntryCounter) {
        json = Json.encodeToString(allPref - e.into())
    }
    
    override fun deleteAll() {
        json = "[]"
    }
    
    override fun update(e: FoodEntryCounter) {
        json = Json.encodeToString(allPref.map { if (it.foodEntryId == e.food.id) e.into() else it })
    }
}