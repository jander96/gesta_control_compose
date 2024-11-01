package com.devj.gestantescontrolcompose.app.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Message
import androidx.compose.material.icons.automirrored.outlined.Message
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Message
import androidx.compose.material.icons.outlined.Calculate
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Message
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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


    Row(modifier = modifier.background(color = Color.Transparent),horizontalArrangement = Arrangement.Center) {
        Surface(
            shape = MaterialTheme.shapes.medium.copy(CornerSize(20.dp)),
            modifier = modifier.height(48.dp),
            tonalElevation = 4.dp,
            color = MaterialTheme.colorScheme.primaryContainer
        ) {
            val withScreen = LocalConfiguration.current.screenWidthDp
            val  colors = NavigationBarItemColors(
                selectedIconColor = MaterialTheme.colorScheme.surface,
                selectedTextColor = MaterialTheme.colorScheme.surface,
                unselectedIconColor = MaterialTheme.colorScheme.onSurface,
                unselectedTextColor = MaterialTheme.colorScheme.onSurface,
                disabledIconColor = MaterialTheme.colorScheme.onSurface,
                disabledTextColor = MaterialTheme.colorScheme.onSurface,
                selectedIndicatorColor = MaterialTheme.colorScheme.secondary,
            )

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
                            imageVector = Icons.Outlined.Home,
                            contentDescription = stringResource(R.string.home)
                        )
                    },
                    colors = colors
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
                            imageVector = Icons.Outlined.Calculate,
                            contentDescription = stringResource(R.string.calculator)
                        )
                    },
                    colors = colors
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
                            imageVector = Icons.AutoMirrored.Outlined.Message,
                            contentDescription = "Favorite",
                        )
                    },
                    colors = colors
                )
            }
        }
    }
}