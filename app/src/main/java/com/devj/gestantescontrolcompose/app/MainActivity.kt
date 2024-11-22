package com.devj.gestantescontrolcompose.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.devj.gestantescontrolcompose.app.navigation.BottomNavigation
import com.devj.gestantescontrolcompose.app.navigation.Calculator
import com.devj.gestantescontrolcompose.app.navigation.Edition
import com.devj.gestantescontrolcompose.app.navigation.Home
import com.devj.gestantescontrolcompose.app.navigation.Scheduler
import com.devj.gestantescontrolcompose.app.navigation.launchSingleTopTo
import com.devj.gestantescontrolcompose.common.presenter.composables.DefaultSnackbar
import com.devj.gestantescontrolcompose.common.presenter.theme.GestantesControlComposeTheme
import com.devj.gestantescontrolcompose.features.editor.view.editionscreen.EditionPage
import com.devj.gestantescontrolcompose.features.home.ui.homescreen.HomePage
import com.devj.gestantescontrolcompose.features.home.ui.viewmodel.HomeViewModel
import com.devj.gestantescontrolcompose.features.quick_calculator.view.screen.CalculatorPage
import com.devj.gestantescontrolcompose.features.scheduler.presenter.views.screen.MessageSchedulePage
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GestantesControlComposeTheme {
                MyApp()
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyApp(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val focusManager = LocalFocusManager.current
    val snackbarHostState = SnackbarHostState()
    var showAppBar by rememberSaveable { mutableStateOf(true) }
    val navState by navController.currentBackStackEntryAsState()


    LaunchedEffect(navState?.destination?.route ){
        showAppBar = navState?.destination?.route == Home.route ||
                navState?.destination?.route == Calculator.route ||
                navState?.destination?.route == Scheduler.route
    }
    Scaffold(
        snackbarHost = {
            DefaultSnackbar(
                snackbarHostState = snackbarHostState ,
                onDismiss = {
                    snackbarHostState.currentSnackbarData?.dismiss()
                }
            )
        },
        bottomBar = {
            AnimatedVisibility(
                showAppBar,
                enter = slideInVertically(tween(500,200)) {it},
                exit = slideOutVertically(tween(500,200)){it},

                ) {
                BottomNavigation(
                    modifier = modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 4.dp),
                    navState = navState,
                    onDestinationClick = { index ->
                        when (index) {
                            0 -> navController.launchSingleTopTo(Home.route)
                            1 -> navController.launchSingleTopTo(Calculator.route)
                            2 -> navController.launchSingleTopTo(Scheduler.route)
                        }
                    }
                )
            }
        }
    ) {paddingValues->
        Box(modifier = Modifier.padding(paddingValues)) {

            NavHost(
                modifier = modifier
                    .fillMaxSize()
                    .clickable(role = Role.Image) { focusManager.clearFocus() },
                navController = navController,
                startDestination = Home.route,
            ) {
                composable(route = Home.route) {

                    HomePage(
                        onItemClick = {
                            navController.launchSingleTopTo(Edition.passParams(it))

                        },
                        onFABClick = {
                            navController.launchSingleTopTo(route = Edition.passParams(null))
                        },
                        homeViewModel =  hiltViewModel<HomeViewModel>()
                    )
                }
                composable(route = Calculator.route ) {
                    CalculatorPage()
                }
                composable(route = Edition.ROUTE_WITH_PARAMS, arguments = Edition.arguments) { navBackStackEntry->
                    val pregnant = navBackStackEntry.arguments?.getInt(Edition.Arguments.PREGNANT)

                    EditionPage(pregnant, onSaveTap = {
                        navController.popBackStack()
                    })
                }

                composable(route = Scheduler.route){
                    MessageSchedulePage(snackbarHostState = snackbarHostState)
                }
            }
        }
    }

}


