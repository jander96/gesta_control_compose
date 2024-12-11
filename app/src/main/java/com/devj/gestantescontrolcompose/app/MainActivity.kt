package com.devj.gestantescontrolcompose.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.devj.gestantescontrolcompose.app.navigation.AppNavigationState
import com.devj.gestantescontrolcompose.app.navigation.BottomNavigation
import com.devj.gestantescontrolcompose.app.navigation.Calculator
import com.devj.gestantescontrolcompose.app.navigation.Destination
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
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val navigationState = AppNavigationState()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            GestantesControlComposeTheme {
                MyApp(navigationState = navigationState)
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyApp(modifier: Modifier = Modifier, navigationState: AppNavigationState = AppNavigationState()) {
    val navController = rememberNavController()
    val destination by navigationState.destination.collectAsState()
    val focusManager = LocalFocusManager.current
    val snackbarHostState = SnackbarHostState()
    var showAppBar by rememberSaveable { mutableStateOf(true) }
    val navState by navController.currentBackStackEntryAsState()


    LaunchedEffect(destination){
        showAppBar = (destination is Home || destination is Calculator || destination is Scheduler) //  TODO implement logic to show and hide bottom appbar
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
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp),
                    navState = navState,
                    onDestinationClick = { index ->
                        when (index) {
                            0  -> {
                                navController.launchSingleTopTo(Home)
                                navigationState.navigate(Home)
                            }
                            1 -> {
                                navController.launchSingleTopTo(Calculator)
                                navigationState.navigate(Calculator)

                            }
                            2 -> {
                                navController.launchSingleTopTo(Scheduler)
                                navigationState.navigate(Scheduler)
                            }
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
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) { focusManager.clearFocus() },
                navController = navController,
                startDestination = Destination.START_DESTINATION,
            ) {

                composable<Home> {

                    HomePage(
                        onItemClick = {
                            navController.launchSingleTopTo(Edition(it.id))
                            navigationState.navigate(Edition(it.id))

                        },
                        onFABClick = {
                            navController.launchSingleTopTo(Edition())
                            navigationState.navigate(Edition())
                        },
                        homeViewModel =  hiltViewModel<HomeViewModel>()
                    )
                }
                composable<Calculator> {
                    CalculatorPage()
                }
                composable<Edition> { navBackStackEntry->
                    val edition = navBackStackEntry.toRoute<Edition>()

                    EditionPage(edition.pregnantId, onSaveTap = {
                        navController.popBackStack()
                        navigationState.navigate(Home)
                    })
                }

                composable<Scheduler>{
                    MessageSchedulePage(snackbarHostState = snackbarHostState)
                }
            }
        }
    }

}


