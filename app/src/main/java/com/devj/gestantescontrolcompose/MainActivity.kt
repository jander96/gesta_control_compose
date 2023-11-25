package com.devj.gestantescontrolcompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.devj.gestantescontrolcompose.navigation.Calculator
import com.devj.gestantescontrolcompose.navigation.Edition
import com.devj.gestantescontrolcompose.navigation.Home
import com.devj.gestantescontrolcompose.navigation.launchSingleTopTo
import com.devj.gestantescontrolcompose.ui.screens.calculatorscreen.CalculatorPage
import com.devj.gestantescontrolcompose.ui.screens.editionscreen.EditionPage
import com.devj.gestantescontrolcompose.ui.screens.homescreen.HomePage
import com.devj.gestantescontrolcompose.ui.screens.homescreen.HomeViewModel
import com.devj.gestantescontrolcompose.ui.theme.GestantesControlComposeTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GestantesControlComposeTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MyApp()
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyApp(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    var showAppBar by rememberSaveable {
        mutableStateOf(true)
    }
    Scaffold(modifier = modifier,
        bottomBar = {
            BottomNavigation(modifier = modifier) { index ->
                when (index) {
                    0 -> navController.launchSingleTopTo(route = "home")
                    1 -> navController.launchSingleTopTo(route = "calculator")
                }

            }
        }
    ) { paddingValues ->

        NavHost(
            navController = navController,
            startDestination = Home().route,
            modifier = modifier.padding(paddingValues)
        ) {
            composable(route = Home().route) {
                HomePage(
                    onItemClick = {
                        navController.navigate(Edition.passParams(it))
                        showAppBar = false
                    },
                    onFABClick = {
                        navController.navigate(route = Edition.passParams(null))
                        showAppBar = false
                    },
                    homeViewModel = hiltViewModel<HomeViewModel>()

                )
            }
            composable(route = Calculator().route ) {
                CalculatorPage()
            }
            composable(route = Edition.routeWithParams, arguments = Edition.arguments) {navBackStackEntry->

                val pregnant = navBackStackEntry.arguments?.getInt(Edition.Arguments.pregnant)

                EditionPage(pregnant){
                    navController.popBackStack()
                }
            }
        }
    }
}

@Composable
fun BottomNavigation(modifier: Modifier = Modifier, onDestinationClick: (index: Int) -> Unit) {

    BottomAppBar(
        modifier = modifier,

        ) {
        var index by rememberSaveable { mutableStateOf(0) }

        NavigationBarItem(
            selected = index == 0,
            onClick = {
                index = 0
                onDestinationClick(index)
            },
            icon = {
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = "home"
                )
            },
            label = { Text("Home") }
        )

        NavigationBarItem(
            selected = index == 1,
            onClick = {
                index = 1
                onDestinationClick(index)
            },
            icon = {
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = "calculadora"
                )
            },
            label = { Text("Calculadora") }
        )
    }
}

