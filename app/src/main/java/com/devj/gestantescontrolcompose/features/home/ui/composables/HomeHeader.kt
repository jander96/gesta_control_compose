package com.devj.gestantescontrolcompose.features.home.ui.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.devj.gestantescontrolcompose.R
import com.devj.gestantescontrolcompose.features.home.domain.model.Stats

@Composable
fun HomeHeader(
    modifier: Modifier = Modifier,
    stats: Stats = Stats(),
    onSearch: (String)->Unit,
) {
    val withScreen = LocalConfiguration.current.screenWidthDp
    var query by remember { mutableStateOf("") }

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()) {
                TextField(
                    value = query,
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = stringResource(R.string.search_bar),
                        )
                    },
                    modifier = modifier
                        .width((withScreen * 0.6).dp)
                        .height(48.dp),
                    shape = MaterialTheme.shapes.small.copy(CornerSize(50.dp)),
                    onValueChange = {
                        query = it
                        onSearch(it)
                    },
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                    )
                )
        }


        Text(
            stringResource(R.string.stats),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = modifier.padding(bottom = 12.dp, top = 8.dp)
        )
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            val cardSize = (withScreen * 0.18).dp
            StatCard(
                value = stats.total,
                label = stringResource(R.string.total),
                cardColor = MaterialTheme.colorScheme.primaryContainer,
                size = cardSize
            )
            StatCard(
                value = stats.onRisk,
                label = stringResource(R.string.on_risk),
                cardColor = MaterialTheme.colorScheme.primaryContainer,
                size = cardSize
            )
            StatCard(
                value = stats.onFinalPeriod,
                label = stringResource(R.string.on_last_period),
                cardColor = MaterialTheme.colorScheme.primaryContainer,
                size = cardSize
            )
        }
        Spacer(modifier = Modifier.height(16.dp))

    }
}