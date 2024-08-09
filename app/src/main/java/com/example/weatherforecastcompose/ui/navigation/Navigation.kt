package com.example.weatherforecastcompose.ui.navigation

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.weatherforecastcompose.ui.screens.favorites.FavoritesRoute
import com.example.weatherforecastcompose.ui.screens.settings.SettingsRoute
import com.example.weatherforecastcompose.ui.screens.weather.WeatherRoute

@Composable
fun AppNavHost(
    onLocationClick: () -> Unit,
    navController: NavHostController = rememberNavController(),
) {
    Scaffold(
        bottomBar = {
            BottomAppBar(
//                containerColor = MaterialTheme.colorScheme.surface,
                modifier = Modifier.height(80.dp)
            ) {
                val backStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = backStackEntry?.destination?.route

                TopLevelDestination.entries.forEach {
                    NavigationBarItem(
                        selected = currentRoute == it.name,
                        onClick = {
                            navController.navigate(it.name) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                popUpTo(0) { inclusive = true }
                            }
                        },
                        icon = {
                            Icon(modifier = Modifier.size(40.dp),
                                painter = painterResource(
                                    if (currentRoute == it.name) it.selectedIconId
                                    else it.unselectedIconId
                                ),
                                contentDescription = stringResource(id = it.iconTextId)
                            )
                        },
//                        label = {
//                            Text(
////                                color = MaterialTheme.colorScheme.surfaceTint,
//                                text = stringResource(id = it.titleTextId),
//                                fontSize = 12.sp,
//                            )
//                        },
//                        alwaysShowLabel = false,
                        colors = NavigationBarItemDefaults.colors(
                            indicatorColor = Color.Transparent,
                            selectedIconColor = MaterialTheme.colorScheme.onSurface,
                            unselectedIconColor = MaterialTheme.colorScheme.surfaceContainerLowest,
//                            selectedTextColor = MaterialTheme.colorScheme.onSurface,
//                            unselectedTextColor = MaterialTheme.colorScheme.onSurface,
                        )
                    )
                }
            }
        }
    ) { paddingValues ->
//    val statusBarPadding = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
//        val statusBarPadding = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
//        Box(
//            modifier = Modifier.paint(
//                painterResource(id = R.drawable.sky_wallpaper),
//                contentScale = ContentScale.FillBounds
//            )
//        )

        NavHost(
            navController = navController,
            startDestination = TopLevelDestination.Weather.name,
        ) {
            composable(TopLevelDestination.Weather.name) {
                WeatherRoute(
                    onLocationClick = onLocationClick,
                    modifier = Modifier.padding(paddingValues)
                )
            }

            composable(TopLevelDestination.Favorites.name) {
                FavoritesRoute(
                    modifier = Modifier.padding(paddingValues),
                    navController = navController
                )
            }

            composable(TopLevelDestination.Settings.name) {
                SettingsRoute(
                    modifier = Modifier.padding(paddingValues)
                )
            }
        }
    }
}