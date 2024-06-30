package com.devj.gestantescontrolcompose.features.home.ui.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.devj.gestantescontrolcompose.common.extensions.Spacer4

@Composable
fun DeleteDialog(
    onAccept: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    Dialog(
        onDismissRequest = onDismiss,
    ) {
        Card() {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Text("¿Estás seguro de eliminar?", style = MaterialTheme.typography.titleMedium)
                Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth().padding(end = 8.dp)) {
                    TextButton(onClick = onDismiss) {
                        Text("No")
                    }
                    Spacer4()
                    TextButton(onClick = onAccept) {
                        Text("Si")
                    }
                }
            }
        }

    }
}