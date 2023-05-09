package hu.ait.andwallet

import SummaryScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import hu.ait.andwallet.ui.screen.mainscreen.MainScreen
import hu.ait.andwallet.ui.theme.HighLowGameComposeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HighLowGameComposeTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MyAppNavHost()
                }
            }
        }
    }
}

@Composable
fun MyAppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = "mainmenu"
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination
    ) {
        composable("mainmenu") { MainScreen(
            onNavigateToSummary = {navController.navigate("summary")},
            navController = navController
        )
        }
        //composable("game") { GameScreen()}
        composable("summary?income={income}&expense={expense}",
            arguments = listOf(
                navArgument("income") {
                defaultValue = 0
                type = NavType.FloatType
            },
                navArgument("expense") {
                    defaultValue = 0
                    type = NavType.FloatType
                })
        ) {
            val income = it.arguments?.getFloat("income") ?: 0
            val expense = it.arguments?.getFloat("expense") ?: 0
            SummaryScreen(income as Float, expense as Float, navController)
        }

    }
}
