package com.devj.gestantescontrolcompose.features.scheduler.presenter.views.composables

import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.devj.gestantescontrolcompose.R
import com.devj.gestantescontrolcompose.common.extensions.Spacer16
import com.devj.gestantescontrolcompose.common.extensions.Spacer4
import com.devj.gestantescontrolcompose.common.presenter.composables.UriImage
import com.devj.gestantescontrolcompose.common.presenter.model.PregnantUI

@Composable
fun AddresseePicker(
    onCancel: () -> Unit,
    onConfirm: (List<PregnantUI>) -> Unit,
    addressees: List<PregnantUI>,
    selected: List<String>?,
) {
    var remittersSelected  by remember {
        mutableStateOf<List<PregnantUI>>(listOf<PregnantUI>())
    }
    var isAllSelected by rememberSaveable {
        mutableStateOf(false)
    }
    val height = LocalConfiguration.current.screenHeightDp

    LaunchedEffect(key1 = selected,){
        selected?.let { remittersSelected = addressees.filter { it.phoneNumber in selected } }
    }
    Dialog(onDismissRequest = onCancel) {
        Surface(shape = MaterialTheme.shapes.medium) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                    Text(
                        modifier = Modifier.padding(16.dp),
                        text = stringResource(R.string.addressee),
                        style = MaterialTheme.typography.titleMedium
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = isAllSelected,
                            onCheckedChange = {
                                remittersSelected = if (it) {
                                    isAllSelected = true
                                    addressees
                                } else {
                                    isAllSelected = false
                                    emptyList()
                                }
                            },
                        )
                        Text(
                            text = stringResource(R.string.all),
                            style = MaterialTheme.typography.labelMedium
                        )
                        Spacer16()
                    }
                }
                LazyColumn(modifier = Modifier
                    .padding(16.dp)
                    .height((height * 0.6).dp)
                ) {
                    items(addressees) { pregnant ->
                        RemitterItem(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(2.dp),
                            checked = pregnant in remittersSelected,
                            onSelected = { remittersSelected = listOf(*remittersSelected.toTypedArray(),it) },
                            onDeselected = { remitter->
                                remittersSelected = listOf(
                                    *remittersSelected
                                        .toMutableList()
                                        .apply { remove(remitter) }
                                        .toTypedArray()
                                )
                            },
                            remitter = pregnant
                        )
                    }
                }
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    TextButton(onClick = onCancel) {
                        Text(text = stringResource(id = R.string.cancel))
                    }
                    TextButton(onClick = {
                        onConfirm(remittersSelected)
                    }) {
                        Text(text = stringResource(id = R.string.ok))
                    }
                }
            }
        }

    }


}

@Composable
fun RemitterItem(
    modifier: Modifier= Modifier,
    checked: Boolean,
    onSelected: (PregnantUI)->Unit,
    onDeselected: (PregnantUI)->Unit,
    remitter: PregnantUI) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
        ) {
        Checkbox(
            checked = checked,
            onCheckedChange = {
                if (it){
                    onSelected(remitter)
                }else{
                    onDeselected(remitter)
                }
            },
        )
        UriImage(imageUri = if(remitter.photo.isBlank()) null else Uri.parse(remitter.photo) , placeholder = R.drawable.woman_avatar, size = 32.dp)
        Spacer4()
        Text(
            text = "${remitter.name} ${remitter.lastName}",
            style = MaterialTheme.typography.labelMedium
        )
    }
}
