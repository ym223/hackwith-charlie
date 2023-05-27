package jp.chocofac.charlie

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import dagger.hilt.android.AndroidEntryPoint
import jp.chocofac.charlie.data.service.senryu.Senryu
import jp.chocofac.charlie.ui.page.CreateSenryuScreen
import jp.chocofac.charlie.ui.page.HomeScreen
import jp.chocofac.charlie.ui.page.LoginScreen
import jp.chocofac.charlie.ui.page.PostedSenryuScreen
import jp.chocofac.charlie.ui.page.RankingScreen
import jp.chocofac.charlie.ui.theme.CharlieTheme

val LocalNavController = staticCompositionLocalOf<NavHostController> {
    error("No Current NavController")
}

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CharlieTheme {
                val navController = rememberNavController()

                CompositionLocalProvider(
                    LocalNavController provides navController
                ) {
                    Scaffold(
                        bottomBar = {
                            CharlieBottomNavigation()
                        }
                    ) { paddingValue ->
                        NavHost(
                            navController = navController,
                            startDestination = NavItem.LoginScreen.name,
                            modifier = Modifier.padding(paddingValue)
                        ) {
                            composable(NavItem.LoginScreen.name) {
                                LoginScreen()
                            }
                            composable(NavItem.HomeScreen.name) {
                                HomeScreen()
                            }
                            composable(NavItem.RankingScreen.name) {
                                RankingScreen()
                            }
                            composable(NavItem.SenryuScreen.name) {
                                CreateSenryuScreen()
                            }
                            composable(
                                route = "${NavItem.PostScreen.name}/{first}/{second}/{last}",
                                arguments = listOf(
                                    navArgument("first") { type = NavType.StringType },
                                    navArgument("second") { type = NavType.StringType },
                                    navArgument("last") { type = NavType.StringType }
                                )
                            ) { backStackEntry ->
                                val first = backStackEntry.arguments!!.getString("first")
                                val second = backStackEntry.arguments!!.getString("second")
                                val last = backStackEntry.arguments!!.getString("last")

                                if (first != null) {
                                    if (second != null) {
                                        if (last != null) {
                                            PostedSenryuScreen(first = first, second = second, last = last)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

val defaultNavigationItems = listOf(
    BottomNavigationItem(
        NavItem.HomeScreen.name,
        R.string.home_navigation_label,
        Icons.Default.Home
    ),
    BottomNavigationItem(
        NavItem.RankingScreen.name,
        R.string.ranking_navigation_label,
        Icons.Default.List
    )
)

@Composable
fun CharlieBottomNavigation(items: List<BottomNavigationItem> = defaultNavigationItems) {
    val navController = LocalNavController.current
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val homeOrHistory = currentDestination?.hierarchy?.any {
        it.route == NavItem.HomeScreen.name || it.route == NavItem.RankingScreen.name
    }

    if (homeOrHistory == true) {
        NavigationBar {
            items.forEach { item ->
                NavigationBarItem(
                    icon = {
                        Icon(item.icon, contentDescription = item.route)
                    },
                    selected = currentDestination.hierarchy.any { it.route == item.route },
                    onClick = {
                        navController.navigate(item.route)
                    },
                    label = {
                        Text(stringResource(id = item.resourceId))
                    }
                )
            }
        }
    }
}

data class BottomNavigationItem(
    val route: String,
    @StringRes val resourceId: Int,
    val icon: ImageVector
)