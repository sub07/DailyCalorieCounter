package dev.mpardo.dailycaloriecounter.repository

import android.content.SharedPreferences
import dev.mpardo.dailycaloriecounter.model.FoodRecord
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

interface FoodRecordRepository : Repository<FoodRecord, Long>

private const val FoodRecordKey = "dev.mpardo.dailycaloriecoutner.FoodRecordJsonDb"
private const val FoodRecordNextIdKey = "dev.mpardo.dailycaloriecoutner.FoodRecordNextId"

class SharedPreferenceFoodRecordRepository : FoodRecordRepository, KoinComponent {
    
    private val foodEntryRepository by inject<FoodEntryRepository>()
    private val sharedPreferences by inject<SharedPreferences>()
    
    @Serializable
    data class FoodRecordPref(
        val id: Long,
        var foodEntryId: Long,
        val mass: Int,
        val date: Long,
    ) {
        fun into(foodEntryRepository: FoodEntryRepository) = FoodRecord(id, foodEntryRepository.get(this.foodEntryId)!!, mass, date)
        
        companion object {
            fun from(record: FoodRecord) = FoodRecordPref(record.id, record.food.id, record.mass, record.date)
        }
    }
    
    private fun FoodRecord.into(id: Long = this.id) = FoodRecordPref.from(this).copy(id = id)
    
    private var json
        get() = sharedPreferences.getString(FoodRecordKey, "[]") ?: "[]"
        set(value) {
            sharedPreferences.edit().putString(FoodRecordKey, value).apply()
        }
    
    private val allPref: List<FoodRecordPref> get() = Json.decodeFromString(json)
    
    override val nextId: Long
        get() {
            val nextId = sharedPreferences.getLong(FoodRecordNextIdKey, 0)
            sharedPreferences.edit().putLong(FoodRecordNextIdKey, nextId + 1).apply()
            return nextId
        }
    
    override val all: List<FoodRecord>
        get() = allPref.map { it.into(foodEntryRepository) }
    
    override fun get(key: Long): FoodRecord? = allPref.find { it.id == key }?.into(foodEntryRepository)
    
    override fun add(e: FoodRecord) {
        json = Json.encodeToString(allPref + e.into(id = nextId))
    }
    
    override fun delete(e: FoodRecord) {
        json = Json.encodeToString(allPref - e.into())
    }
    
    override fun deleteAll() {
        json = "[]"
    }
    
    override fun update(e: FoodRecord) {
        json = Json.encodeToString(allPref.map { if (it.id == e.id) e.into() else it })
    }
}