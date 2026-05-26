package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.*
import com.example.data.AppDatabase
import com.example.data.SpedexRepository
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.screens.*
import com.example.viewmodel.SpedexViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Provide core dependencies
        val database = AppDatabase.getDatabase(applicationContext)
        val repository = SpedexRepository(database)

        setContent {
            MyApplicationTheme {
                val factory = remember { SpedexViewModelFactory(repository) }
                val viewmodel: SpedexViewModel = viewModel(factory = factory)
                val navController = rememberNavController()
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                // Hide bottom navigation on scan or pay confirm modes to optimize views and focus actions
                val showBottomNav = currentRoute in listOf("home", "budgets", "reminders", "analytics")

                Scaffold(
                    bottomBar = {
                        if (showBottomNav) {
                            NavigationBar(
                                containerColor = MaterialTheme.colorScheme.surface,
                                tonalElevation = 8.dp,
                                modifier = Modifier.testTag("bottom_nav")
                            ) {
                                val items = listOf(
                                    NavigationItem("home", "Home", Icons.Default.Home, Icons.Outlined.Home),
                                    NavigationItem("budgets", "Budgets", Icons.Default.AccountBalanceWallet, Icons.Outlined.AccountBalanceWallet),
                                    NavigationItem("reminders", "Reminders", Icons.Default.NotificationsActive, Icons.Outlined.Notifications),
                                    NavigationItem("analytics", "Analytics", Icons.Default.Insights, Icons.Outlined.Leaderboard)
                                )

                                items.forEach { item ->
                                    val isSelected = currentRoute == item.route
                                    NavigationBarItem(
                                        selected = isSelected,
                                        label = { Text(item.title, style = MaterialTheme.typography.labelSmall) },
                                        icon = {
                                            Icon(
                                                imageVector = if (isSelected) item.selectedIcon else item.unselectedIcon,
                                                contentDescription = item.title
                                            )
                                        },
                                        onClick = {
                                            if (currentRoute != item.route) {
                                                navController.navigate(item.route) {
                                                    popUpTo(navController.graph.findStartDestination().id) {
                                                        saveState = true
                                                    }
                                                    launchSingleTop = true
                                                    restoreState = true
                                                }
                                            }
                                        },
                                        modifier = Modifier.testTag("nav_item_${item.route}")
                                    )
                                }
                            }
                        }
                    },
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = "home",
                        modifier = Modifier.fillMaxSize()
                    ) {
                        composable("home") {
                            HomeScreen(navController, viewmodel, Modifier.padding(bottom = if (showBottomNav) 0.dp else innerPadding.calculateBottomPadding()))
                        }
                        composable("scanner") {
                            ScannerScreen(navController, viewmodel)
                        }
                        composable("pay_confirm") {
                            PayConfirmScreen(navController, viewmodel)
                        }
                        composable("budgets") {
                            BudgetsScreen(navController, viewmodel, Modifier.padding(bottom = if (showBottomNav) 0.dp else innerPadding.calculateBottomPadding()))
                        }
                        composable("reminders") {
                            RemindersScreen(navController, viewmodel, Modifier.padding(bottom = if (showBottomNav) 0.dp else innerPadding.calculateBottomPadding()))
                        }
                        composable("analytics") {
                            AnalyticsScreen(navController, viewmodel, Modifier.padding(bottom = if (showBottomNav) 0.dp else innerPadding.calculateBottomPadding()))
                        }
                    }
                }
            }
        }
    }
}

data class NavigationItem(
    val route: String,
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
)

@Suppress("UNCHECKED_CAST")
class SpedexViewModelFactory(private val repository: SpedexRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SpedexViewModel::class.java)) {
            return SpedexViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
