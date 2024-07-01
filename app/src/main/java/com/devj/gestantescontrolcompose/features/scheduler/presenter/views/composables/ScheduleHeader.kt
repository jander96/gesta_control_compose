package com.devj.gestantescontrolcompose.features.scheduler.presenter.views.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
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
import com.devj.gestantescontrolcompose.common.extensions.Spacer8
import com.devj.gestantescontrolcompose.common.extensions.toCurrency
import com.devj.gestantescontrolcompose.common.presenter.composables.UriImage

@Composable
fun ScheduleHeader(
    modifier: Modifier = Modifier,
    date: String,
    messageQuantity: Int,
    username: String
) {
    Column(modifier = modifier) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {


                Text(text = date ,
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold))


        }
        Spacer16()

        ScheduleCard(
            modifier = Modifier.fillMaxWidth(),
            messageQuantity = messageQuantity,
        )
    }

}

@Composable
fun ScheduleCard(
    modifier: Modifier = Modifier,
    messageQuantity: Int,
    ) {
    Card(modifier = modifier) {
        val title = when(messageQuantity){
            0-> stringResource(R.string.no_message)
            1-> stringResource(R.string.message_quantity_single, messageQuantity)
            else -> stringResource(R.string.message_quantity, messageQuantity)
        }
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text =title, style = MaterialTheme.typography.titleMedium)
        }
    }
}

@Composable
fun FinanceRow(
    text: String,
    price: Float,
    modifier: Modifier = Modifier
) {
    Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = modifier.fillMaxWidth()) {
        Text(text,style = MaterialTheme.typography.bodyMedium)
        Text(price.toCurrency(), style = MaterialTheme.typography.labelMedium)
    }
}
