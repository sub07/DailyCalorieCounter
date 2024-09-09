package me.mpardo.dailycaloriecounter

import android.content.Context
import android.content.SharedPreferences
import me.mpardo.dailycaloriecounter.repository.FoodEntryCounterRepository
import me.mpardo.dailycaloriecounter.repository.FoodEntryRepository
import me.mpardo.dailycaloriecounter.repository.FoodRecordRepository
import me.mpardo.dailycaloriecounter.repository.SharedPreferenceFoodEntryCounterRepository
import me.mpardo.dailycaloriecounter.repository.SharedPreferenceFoodEntryRepository
import me.mpardo.dailycaloriecounter.repository.SharedPreferenceFoodRecordRepository
import me.mpardo.dailycaloriecounter.repository.SharedPreferenceSettingsRepository
import me.mpardo.dailycaloriecounter.viewmodel.FoodEntryViewModel
import me.mpardo.dailycaloriecounter.viewmodel.FoodRecordViewModel
import me.mpardo.dailycaloriecounter.viewmodel.SettingsViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    single { SharedPreferenceSettingsRepository() }
    single<FoodEntryRepository> { SharedPreferenceFoodEntryRepository() }
    single<FoodRecordRepository> { SharedPreferenceFoodRecordRepository() }
    single<FoodEntryCounterRepository> { SharedPreferenceFoodEntryCounterRepository() }

    single<SharedPreferences> {
        androidApplication().getSharedPreferences(
            "business",
            Context.MODE_PRIVATE
        )
    }

    viewModel { FoodRecordViewModel() }
    viewModel { FoodEntryViewModel() }
    viewModel { SettingsViewModel() }
}