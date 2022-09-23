package dev.mpardo.dailycaloriecounter

import android.content.Context
import android.content.SharedPreferences
import dev.mpardo.dailycaloriecounter.repository.*
import dev.mpardo.dailycaloriecounter.viewmodel.FoodEntryViewModel
import dev.mpardo.dailycaloriecounter.viewmodel.FoodRecordViewModel
import dev.mpardo.dailycaloriecounter.viewmodel.SettingsViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    single { SharedPreferenceSettingsRepository() }
    single<FoodEntryRepository> { SharedPreferenceFoodEntryRepository() }
    single<FoodRecordRepository> { SharedPreferenceFoodRecordRepository() }
    single<FoodEntryCounterRepository> { SharedPreferenceFoodEntryCounterRepository() }
    
    single<SharedPreferences> { androidApplication().getSharedPreferences("business", Context.MODE_PRIVATE) }
    
    viewModel { FoodRecordViewModel() }
    viewModel { FoodEntryViewModel() }
    viewModel { SettingsViewModel() }
}