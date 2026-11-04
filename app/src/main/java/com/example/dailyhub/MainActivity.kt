package com.example.dailyhub

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.dailyhub.ui.AboutScreen
import com.example.dailyhub.ui.DailyHubTheme
import com.example.dailyhub.ui.HomeScreen
import com.example.dailyhub.ui.TasksScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent { DailyHubApp() }
    }
}

private enum class Dest(val route: String, val label: String, val icon: ImageVector) {
    Home("home", "Home", Icons.Filled.Home),
    Tasks("tasks", "Tasks", Icons.Filled.List),
    About("about", "About", Icons.Filled.Info)
}

@Composable
fun DailyHubApp(vm: AppViewModel = viewModel()) {
    val darkPref by vm.darkMode.collectAsState()
    val isDark = darkPref ?: isSystemInDarkTheme()

    DailyHubTheme(darkTheme = isDark) {
        val nav = rememberNavController()
        val backStack by nav.currentBackStackEntryAsState()
        val currentRoute = backStack?.destination?.route

        Scaffold(
            bottomBar = {
                NavigationBar {
                    Dest.values().forEach { d ->
                        NavigationBarItem(
                            selected = currentRoute == d.route,
                            onClick = {
                                nav.navigate(d.route) {
                                    popUpTo(nav.graph.findStartDestination().id) { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = { Icon(d.icon, contentDescription = d.label) },
                            label = { Text(d.label) }
                        )
                    }
                }
            }
        ) { padding ->
            NavHost(
                navController = nav,
                startDestination = Dest.Home.route,
                modifier = Modifier.padding(padding)
            ) {
                composable(Dest.Home.route) { HomeScreen() }
                composable(Dest.Tasks.route) { TasksScreen(vm) }
                composable(Dest.About.route) { AboutScreen(isDark = isDark, onDarkChange = vm::setDarkMode) }
            }
        }
    }
}
