package dev.mpardo.dailycaloriecounter

import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import dev.mpardo.dailycaloriecounter.ui.CustomNavHost
import dev.mpardo.dailycaloriecounter.ui.Screen
import dev.mpardo.dailycaloriecounter.ui.theme.DailyCaloryCounterTheme
import dev.mpardo.dailycaloriecounter.viewmodel.FoodEntryViewModel
import dev.mpardo.dailycaloriecounter.viewmodel.FoodRecordViewModel
import dev.mpardo.dailycaloriecounter.viewmodel.SettingsViewModel
import org.koin.android.ext.android.get
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class DailyCalorieCounterActivity : ComponentActivity() {
    
    val foodEntryViewModel: FoodEntryViewModel by viewModel()
    val foodRecordViewModel: FoodRecordViewModel by viewModel()
    val settingsViewModel: SettingsViewModel by viewModel()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startKoin {
            androidContext(applicationContext)
            androidLogger(Level.DEBUG)
            modules(viewModelModule)
        }
        
        val showWelcomePage = get<SharedPreferences>().run {
            getBoolean("showWelcomePage", true)
        }
        
        setContent {
            DailyCaloryCounterTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
                    CustomNavHost(
                        foodEntryViewModel = foodEntryViewModel,
                        foodRecordViewModel = foodRecordViewModel,
                        settingsViewModel = settingsViewModel,
                        startDestination = if (showWelcomePage) Screen.Welcome.route else Screen.Dashboard.route
                    )
                }
            }
        }
    }
}