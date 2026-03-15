package com.dinnerspinner.app.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Casino
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.dinnerspinner.app.ui.meals.AddEditMealScreen
import com.dinnerspinner.app.ui.meals.AddMealPickerScreen
import com.dinnerspinner.app.ui.meals.MyMealsScreen
import com.dinnerspinner.app.ui.plan.MealPlanScreen
import com.dinnerspinner.app.ui.spinner.SpinnerScreen
import com.dinnerspinner.app.ui.theme.CasinoGold
import com.dinnerspinner.app.ui.theme.CasinoGoldDark

private data class BottomNavItem(
    val label: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
    val screen: Screen
)

private val bottomNavItems = listOf(
    BottomNavItem("Spin", Icons.Filled.Casino, Screen.Spinner),
    BottomNavItem("Meals", Icons.Filled.MenuBook, Screen.MyMeals),
    BottomNavItem("Plan", Icons.Filled.CalendarMonth, Screen.MealPlan)
)

@Composable
fun AppNavigation() {
    val navController: NavHostController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val showBottomBar = currentRoute in listOf(
        Screen.Spinner.route,
        Screen.MyMeals.route,
        Screen.MealPlan.route
    )

    Scaffold(
        containerColor = Color.Transparent,
        bottomBar = {
            if (showBottomBar) {
                NavigationBar(
                    containerColor = Color(0xFF0A0804),
                    contentColor = CasinoGold,
                ) {
                    bottomNavItems.forEach { item ->
                        val selected = currentRoute == item.screen.route
                        NavigationBarItem(
                            icon = {
                                Icon(item.icon, contentDescription = item.label)
                            },
                            label = {
                                Text(
                                    item.label,
                                    style = TextStyle(
                                        fontSize = 9.sp,
                                        letterSpacing = 1.sp,
                                        fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal
                                    )
                                )
                            },
                            selected = selected,
                            onClick = {
                                navController.navigate(item.screen.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = CasinoGold,
                                selectedTextColor = CasinoGold,
                                indicatorColor = Color(0xFF3A2808),
                                unselectedIconColor = CasinoGoldDark,
                                unselectedTextColor = CasinoGoldDark
                            )
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Spinner.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Spinner.route) {
                SpinnerScreen()
            }
            composable(Screen.MyMeals.route) {
                MyMealsScreen(
                    onAddMeal = { navController.navigate(Screen.AddMealPicker.route) },
                    onEditMeal = { mealId -> navController.navigate(Screen.AddEditMeal.createRoute(mealId)) }
                )
            }
            composable(Screen.AddMealPicker.route) {
                AddMealPickerScreen(
                    onNavigateBack = { navController.popBackStack() },
                    onAddCustomMeal = { navController.navigate(Screen.AddEditMeal.createRoute()) }
                )
            }
            composable(
                route = Screen.AddEditMeal.route,
                arguments = listOf(navArgument("mealId") {
                    type = NavType.LongType
                    defaultValue = -1L
                })
            ) { backStackEntry ->
                val mealId = backStackEntry.arguments?.getLong("mealId")?.takeIf { it != -1L }
                AddEditMealScreen(
                    mealId = mealId,
                    onNavigateBack = { navController.popBackStack() }
                )
            }
            composable(Screen.MealPlan.route) {
                MealPlanScreen()
            }
        }
    }
}
