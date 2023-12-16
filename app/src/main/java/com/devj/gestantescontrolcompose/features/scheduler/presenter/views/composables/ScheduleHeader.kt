package com.devj.gestantescontrolcompose.features.scheduler.presenter.views.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.devj.gestantescontrolcompose.R
import com.devj.gestantescontrolcompose.common.extensions.Spacer16
import com.devj.gestantescontrolcompose.common.extensions.Spacer4
import com.devj.gestantescontrolcompose.common.extensions.Spacer8
import com.devj.gestantescontrolcompose.common.extensions.toCurrency
import com.devj.gestantescontrolcompose.common.extensions.toLabelDate
import com.devj.gestantescontrolcompose.common.ui.composables.UriImage

@Composable
fun ScheduleHeader(
    modifier: Modifier = Modifier,
    date: String,
    messageQuantity: Int,
    currentCost: Double,
    accumulatedCost: Double,
    totalCost: Double,
    username: String
) {
    Column(modifier = modifier) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            UriImage(imageUri = null, placeholder = R.drawable.woman_avatar, size = 40.dp )
            Spacer8()
            Column {
                Text(text = date.toLabelDate(), style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold))
                Text(text = username, style = MaterialTheme.typography.labelLarge)
            }
        }
        Spacer16()

        ScheduleCard(
            messageQuantity = messageQuantity,
            currentCost = currentCost,
            accumulatedCost = accumulatedCost,
            totalCost = totalCost
        )
    }

}

@Composable
fun ScheduleCard(
    modifier: Modifier = Modifier,
    messageQuantity: Int,
    currentCost: Double,
    accumulatedCost: Double,
    totalCost: Double,
    ) {
    Card(modifier = modifier) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = stringResource(R.string.message_quantity, messageQuantity), style = MaterialTheme.typography.titleMedium)
            Spacer4()
            Divider()
            Spacer4()
            FinanceRow(text = stringResource(R.string.cost), price = currentCost)
            FinanceRow(text = stringResource(R.string.acumm), price = accumulatedCost)
            FinanceRow(text = stringResource(R.string.total), price = totalCost)
        }
    }
}

@Composable
fun FinanceRow(
    text: String,
    price: Double,
    modifier: Modifier = Modifier
) {
    Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = modifier.fillMaxWidth()) {
        Text(text,style = MaterialTheme.typography.bodyMedium)
        Text(price.toCurrency(), style = MaterialTheme.typography.labelMedium)
    }
}
