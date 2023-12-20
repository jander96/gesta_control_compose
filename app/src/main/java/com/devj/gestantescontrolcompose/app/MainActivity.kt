package com.devj.gestantescontrolcompose.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavBackStackEntry
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.devj.gestantescontrolcompose.R
import com.devj.gestantescontrolcompose.app.navigation.Calculator
import com.devj.gestantescontrolcompose.app.navigation.Edition
import com.devj.gestantescontrolcompose.app.navigation.Home
import com.devj.gestantescontrolcompose.app.navigation.Scheduler
import com.devj.gestantescontrolcompose.app.navigation.launchSingleTopTo
import com.devj.gestantescontrolcompose.common.presenter.theme.GestantesControlComposeTheme
import com.devj.gestantescontrolcompose.features.editor.view.editionscreen.EditionPage
import com.devj.gestantescontrolcompose.features.home.ui.homescreen.HomePage
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

    var showAppBar by rememberSaveable {
        mutableStateOf(true)
    }
    val navState by navController.currentBackStackEntryAsState()

    LaunchedEffect(navState?.destination?.route ){
        showAppBar = navState?.destination?.route == Home.route ||
                navState?.destination?.route == Calculator.route ||
                navState?.destination?.route == Scheduler.route
    }
    Box() {

        NavHost(
            modifier = modifier
                .fillMaxSize()
                .clickable { focusManager.clearFocus() },
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
                )
            }
            composable(route = Calculator.route ) {
                CalculatorPage()
            }
            composable(route = Edition.routeWithParams, arguments = Edition.arguments) { navBackStackEntry->
                val pregnant = navBackStackEntry.arguments?.getInt(Edition.Arguments.pregnant)

                EditionPage(pregnant, onSaveTap = {
                    navController.popBackStack()
                })
            }

            composable(route = Scheduler.route){
                MessageSchedulePage()
            }
        }

            AnimatedVisibility(
                showAppBar,
                modifier = modifier.align(Alignment.BottomCenter),
                enter = slideInVertically(tween(500,200)) {it},
                exit = slideOutVertically(tween(500,200)){it}

            ) {
                BottomNavigation(
                    modifier = modifier.padding(4.dp),
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
}

@Composable
fun BottomNavigation(
    modifier: Modifier = Modifier,
    onDestinationClick: (index: Int) -> Unit,
    navState: NavBackStackEntry?
    ) {


    Row(modifier = modifier,horizontalArrangement = Arrangement.Center) {
        Surface(
            shape = MaterialTheme.shapes.medium.copy(CornerSize(50.dp)),
            modifier = modifier.height(48.dp),
            tonalElevation = 4.dp,
            color = MaterialTheme.colorScheme.primaryContainer
        ) {
            val withScreen = LocalConfiguration.current.screenWidthDp

            Row(
                modifier = modifier
                    .width((withScreen * 0.60).dp)
                    .padding(4.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                var index by rememberSaveable { mutableIntStateOf(0) }

                NavigationBarItem(
                    selected = navState?.destination?.route?.lowercase() == Home.route,
                    onClick = {
                        index = 0
                        onDestinationClick(index)
                    },
                    icon = {
                        Icon(
                            modifier = Modifier.size(24.dp),
                            painter = painterResource(R.drawable.ic_home_svg),
                            contentDescription = stringResource(R.string.home)
                        )
                    },
                )

                NavigationBarItem(
                    selected = navState?.destination?.route?.lowercase() == Calculator.route,
                    onClick = {
                        index = 1
                        onDestinationClick(index)
                    },
                    icon = {
                        Icon(
                            modifier = Modifier.size(24.dp),
                            painter = painterResource(R.drawable.ic_calculator_svg),
                            contentDescription = stringResource(R.string.calculator)
                        )
                    },
                )

                NavigationBarItem(
                    selected = navState?.destination?.route?.lowercase() == Scheduler.route,
                    onClick = {
                        index = 2
                        onDestinationClick(index)
                    },
                    icon = {
                        Icon(
                            modifier = Modifier.size(24.dp),
                            painter = painterResource(R.drawable.ic_schedule_svg),
                            contentDescription = stringResource(R.string.scheduler)
                        )
                    },
                )
            }
        }
    }
}

