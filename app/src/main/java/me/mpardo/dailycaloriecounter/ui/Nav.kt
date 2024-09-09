package me.mpardo.dailycaloriecounter.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
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
import me.mpardo.dailycaloriecounter.R
import me.mpardo.dailycaloriecounter.ui.screen.DashboardScreen
import me.mpardo.dailycaloriecounter.ui.screen.FoodCalDbListScreen
import me.mpardo.dailycaloriecounter.ui.screen.FoodRecordListScreen
import me.mpardo.dailycaloriecounter.ui.screen.SettingsScreen
import me.mpardo.dailycaloriecounter.viewmodel.FoodEntryViewModel
import me.mpardo.dailycaloriecounter.viewmodel.FoodRecordViewModel
import me.mpardo.dailycaloriecounter.viewmodel.SettingsViewModel

sealed class Screen(val route: String, val icon: ImageVector?) {
    object Dashboard : Screen("dashboard", FeatherIcons.PieChart)
    object FoodRecord : Screen("records", FeatherIcons.List)
    object FoodDb : Screen("db", FeatherIcons.Database)
    object Settings : Screen("settings", FeatherIcons.Settings)
}

object Screens : Iterable<Screen> {
    private val screens = listOf(
        Screen.Dashboard,
        Screen.FoodRecord,
        Screen.FoodDb,
        Screen.Settings,
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
        }
    }

    Scaffold(bottomBar = {
        BottomNavigation {
            Screens.filter { it.icon != null }.forEach { screen ->
                BottomNavigationItem(selected = navController.currentBackStackEntryAsState().value?.destination?.hierarchy?.any { it.route == screen.route } == true,
                    icon = {
                        Icon(
                            screen.icon!!,
                            "${getNameFromScreen(screen)} navigation button"
                        )
                    },
                    selectedContentColor = MaterialTheme.colors.primary,
                    unselectedContentColor = MaterialTheme.colors.onBackground,
                    label = { Text(getNameFromScreen(screen)) },
                    onClick = {
                        navController.navigate(screen.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    })
            }
        }
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
                FoodRecordListScreen(
                    foodRecordViewModel.records,
                    onRecordDelete = { foodRecordViewModel.deleteEntry(it) })
            }

            composable(Screen.Dashboard.route) {
                DashboardScreen(dailyGoals = settingsViewModel.goals,
                    totalCalorieRecorded = foodRecordViewModel.totalCalorieRecorded,
                    totalProteinRecorded = foodRecordViewModel.totalProteinRecorded,
                    totalFatsRecorded = foodRecordViewModel.totalFatsRecorded,
                    totalCarbohydratesRecorded = foodRecordViewModel.totalCarbohydratesRecorded,
                    totalSaltRecorded = foodRecordViewModel.totalSaltRecorded,
                    nbHourSinceFirstMeal = foodRecordViewModel.nbHourSinceFirstMeal,
                    foodEntries = foodEntryViewModel.calDb,
                    foodEntryUses = foodEntryViewModel.foodDbCounter,
                    onAddFoodUse = { foodEntryViewModel.addOneFoodEntryUse(it) },
                    onAddRecord = { foodRecordViewModel.addFoodEntry(it) },
                    onClearAllRecord = { foodRecordViewModel.deleteAll() })
            }

            composable(Screen.Settings.route) {
                SettingsScreen(settingsViewModel.dailyCalorieGoal,
                    settingsViewModel.dailyProteinGoal,
                    settingsViewModel.dailyFatsGoal,
                    settingsViewModel.dailyCarbohydratesGoal,
                    settingsViewModel.dailySaltGoal,
                    onCalorieGoalUpdate = { settingsViewModel.setNewDailyCalorieGoal(it) },
                    onProteinGoalUpdate = { settingsViewModel.setNewDailyProteinGoal(it) },
                    onFatsGoalUpdate = { settingsViewModel.setNewDailyFatsGoal(it) },
                    onCarbohydratesGoalUpdate = { settingsViewModel.setNewDailyCarbohydratesGoal(it) },
                    onSaltGoalUpdate = { settingsViewModel.setNewDailySaltGoal(it) },
                    onAddCustomCalorieAmount = { foodRecordViewModel.addCustomEnergyRecord(it) })
            }
        }
    }
}