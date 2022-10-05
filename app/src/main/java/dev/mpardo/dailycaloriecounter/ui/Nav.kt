package dev.mpardo.dailycaloriecounter.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import compose.icons.FeatherIcons
import compose.icons.feathericons.Database
import compose.icons.feathericons.List
import compose.icons.feathericons.PieChart
import compose.icons.feathericons.Settings
import dev.mpardo.dailycaloriecounter.R
import dev.mpardo.dailycaloriecounter.ui.screen.*
import dev.mpardo.dailycaloriecounter.viewmodel.FoodEntryViewModel
import dev.mpardo.dailycaloriecounter.viewmodel.FoodRecordViewModel
import dev.mpardo.dailycaloriecounter.viewmodel.SettingsViewModel

sealed class Screen(val route: String, val icon: ImageVector?) {
    object Dashboard : Screen("dashboard", FeatherIcons.PieChart)
    object FoodRecord : Screen("records", FeatherIcons.List)
    object FoodDb : Screen("db", FeatherIcons.Database)
    object Settings : Screen("settings", FeatherIcons.Settings)
    object Welcome : Screen("welcome", null)
}

object Screens : Iterable<Screen> {
    private val screens = listOf(
        Screen.Dashboard,
        Screen.FoodRecord,
        Screen.FoodDb,
        Screen.Settings,
        Screen.Welcome,
    )
    
    override fun iterator() = screens.iterator()
}

@Composable
fun CustomNavHost(
    modifier: Modifier = Modifier,
    foodEntryViewModel: FoodEntryViewModel,
    foodRecordViewModel: FoodRecordViewModel,
    settingsViewModel: SettingsViewModel,
    startDestination: String,
    navController: NavHostController = rememberNavController(),
) {
    val getNameFromScreen = @Composable { s: Screen ->
        when (s) {
            Screen.Dashboard -> stringResource(R.string.dashboard_screen_name)
            Screen.FoodDb -> stringResource(R.string.database_screen_name)
            Screen.FoodRecord -> stringResource(R.string.record_screen_name)
            Screen.Settings -> stringResource(R.string.settings_screen_name)
            Screen.Welcome -> stringResource(R.string.welcome_screen_name)
        }
    }
    
    Scaffold(bottomBar = {
        BottomNavigation {
            Screens.filter { it.icon != null }.forEach { screen ->
                BottomNavigationItem(selected = navController.currentBackStackEntryAsState().value?.destination?.hierarchy?.any { it.route == screen.route } == true,
                                     icon = { Icon(screen.icon!!, "${getNameFromScreen(screen)} navigation button") },
                                     label = { Text(getNameFromScreen(screen)) },
                                     onClick = {
                                         navController.navigate(screen.route) {
                                             popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                             launchSingleTop = true
                                             restoreState = true
                                         }
                                     })
            }
        }
    }, topBar = {
        TopAppBar(title = {
            Screens.forEach { screen ->
                if (navController.currentBackStackEntryAsState().value?.destination?.hierarchy?.any { it.route == screen.route } == true) {
                    Text(getNameFromScreen(screen))
                }
            }
        })
    }) { innerPadding ->
        NavHost(
            modifier = modifier.padding(innerPadding),
            navController = navController,
            startDestination = startDestination,
        ) {
            composable(Screen.FoodDb.route) {
                FoodCalDbListScreen(
                    foodEntryViewModel.calDb,
                    foodRecordViewModel.records,
                    onFoodAdd = { foodEntryViewModel.addFoodEntry(it) },
                    onFoodDelete = { foodEntryViewModel.deleteEntry(it) },
                    onFoodUpdate = { foodEntryViewModel.updateEntry(it) },
                )
            }
            
            composable(Screen.FoodRecord.route) {
                FoodRecordListScreen(foodRecordViewModel.records, onRecordDelete = { foodRecordViewModel.deleteEntry(it) })
            }
            
            composable(Screen.Dashboard.route) {
                DashboardScreen(dailyCalorieGoal = settingsViewModel.dailyCalorieGoal,
                                dailyProteinGoal = settingsViewModel.dailyProteinGoal,
                                totalCalorieRecorded = foodRecordViewModel.totalCalorieRecorded,
                                foodEntries = foodEntryViewModel.calDb,
                                foodEntryUses = foodEntryViewModel.foodDbCounter,
                                totalProteinRecorded = foodRecordViewModel.totalProteinRecorded,
                                onAddFoodUse = { foodEntryViewModel.addOneFoodEntryUse(it) },
                                onAddRecord = { foodRecordViewModel.addFoodEntry(it) },
                                onClearAllRecord = { foodRecordViewModel.deleteAll() })
            }
            
            composable(Screen.Settings.route) {
                SettingsScreen(settingsViewModel.dailyCalorieGoal,
                               settingsViewModel.dailyProteinGoal,
                               onCalorieGoalUpdate = { settingsViewModel.setNewDailyCalorieGoal(it) },
                               onProteinGoalUpdate = { settingsViewModel.setNewDailyProteinGoal(it) })
            }
            
            composable(Screen.Welcome.route) {
                WelcomeScreen(onGoalUpdate = { calorie, protein ->
                    settingsViewModel.setNewDailyCalorieGoal(calorie)
                    settingsViewModel.setNewDailyProteinGoal(protein)
                    settingsViewModel.setWelcomeDone()
                    navController.navigate(Screen.Dashboard.route)
                })
            }
        }
    }
}