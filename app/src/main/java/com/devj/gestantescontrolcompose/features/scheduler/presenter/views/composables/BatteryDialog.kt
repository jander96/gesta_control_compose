package com.devj.gestantescontrolcompose.features.scheduler.presenter.views.composables

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.devj.gestantescontrolcompose.R
import com.devj.gestantescontrolcompose.common.extensions.Spacer8

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BatteryDialog(modifier: Modifier = Modifier, onConfirm : () -> Unit, onDismissRequest : ()-> Unit) {

    BasicAlertDialog(onDismissRequest = onDismissRequest, modifier = modifier.padding(32.dp)) {
        Card() {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    stringResource(R.string.battery_optimization_msg),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.labelSmall
                )


                Spacer8()

                Row {
                    OutlinedButton(modifier = Modifier, onClick = onConfirm) {
                        Text("Ok")
                    }
                }
            }
        }
    }
}