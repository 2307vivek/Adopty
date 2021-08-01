/*
 * MIT License
 *
 * Copyright (c) 2021 Vivek Singh
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.example.androiddevchallenge.ui

import android.content.Context
import android.net.ConnectivityManager
import androidx.core.splashscreen.SplashScreen
import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.androiddevchallenge.ui.navigation.Screen
import com.example.androiddevchallenge.ui.screen.dogDetails.DogDetailScreen
import com.example.androiddevchallenge.ui.screen.home.HomeScreen
import com.example.androiddevchallenge.ui.screen.home.HomeViewModel

@Composable
fun AdoptyApp(splashScreenVisibleCondition: (SplashScreen.KeepOnScreenCondition) -> Unit) {
    val context = LocalContext.current
    var isOnline by remember { mutableStateOf(checkIfOnline(context)) }

    if (isOnline) {
        AdoptyNavigation(splashScreenVisibleCondition)
    } else {
        OfflineDialog { isOnline = checkIfOnline(context) }
    }
}

@Suppress("DEPRECATION")
private fun checkIfOnline(context: Context): Boolean {
    val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val activeNetwork = cm.activeNetworkInfo
    return activeNetwork?.isConnectedOrConnecting == true
}

@Composable
fun AdoptyNavigation(splashScreenVisibleCondition: (SplashScreen.KeepOnScreenCondition) -> Unit) {
    val navController = rememberNavController()
    val viewModel: HomeViewModel = viewModel()

    splashScreenVisibleCondition {
        viewModel.state.value.petState!!.loading
    }

    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
    ) {
        composable(Screen.Home.route) {
            HomeScreen(
                viewModel = viewModel,
                navController = navController,
                navBackStackEntry = it,
            )
        }
        composable(Screen.DogDetail.route) {
            DogDetailScreen(
                viewModel = viewModel,
                navController = navController
            )
        }
    }
}

@Composable
fun CheckData() {
    val context = LocalContext.current
}

@Composable
fun OfflineDialog(onRetry: () -> Unit) {
    AlertDialog(
        onDismissRequest = {},
        title = { Text(text = "No Internet") },
        text = { Text(text = "No internet connection. Turn on Wifi or mobile data.") },
        confirmButton = {
            TextButton(onClick = onRetry) {
                Text("Retry")
            }
        }
    )
}
