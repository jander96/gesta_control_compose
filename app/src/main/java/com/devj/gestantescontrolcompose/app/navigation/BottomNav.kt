package com.devj.gestantescontrolcompose.app.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavBackStackEntry
import com.devj.gestantescontrolcompose.R


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