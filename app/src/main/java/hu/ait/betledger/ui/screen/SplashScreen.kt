package hu.ait.betledger.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import hu.ait.betledger.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource


@Composable
fun SplashScreen(navController: NavController) {
    val scope = rememberCoroutineScope()

    LaunchedEffect(key1 = true) {
        scope.launch {
            delay(3000)
            navController.navigate("main_screen") {
                popUpTo("splash_screen") { inclusive = true } // remove splash screen from back stack
            }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        // Replace 'R.drawable.my_logo' with the resource ID of your logo
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "App Logo"
        )
    }
}
