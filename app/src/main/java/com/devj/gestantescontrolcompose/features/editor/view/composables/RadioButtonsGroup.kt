package com.devj.gestantescontrolcompose.features.editor.view.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle

@Composable
fun<T: Any> RadioButtonsGroup(
    modifier: Modifier = Modifier,
    options: Set<RadioOption<T>>,
    selectedIndexByDefault: Int? = null,
    onSelect: (T)->Unit
){
    var selected by rememberSaveable {
        mutableStateOf<Int?>(null)
    }
    if (
        selectedIndexByDefault != null &&
        selectedIndexByDefault in options.indices
    ) {
        selected = selectedIndexByDefault
    }
    Column(modifier = modifier) {
        options.forEachIndexed {index,option->
            RadioTail(
                selected = index == selected,
                onClick = {
                    selected = index
                    selected?.let { onSelect(option.data) }
                          },
                text = option.label
            )
        }
    }
}

data class RadioOption<T: Any>(val label: String, val data: T)

@Composable
fun RadioTail(
    modifier: Modifier = Modifier,
    selected : Boolean,
    onClick: ()->Unit,
    text: String,
    textStyle: TextStyle = MaterialTheme.typography.labelMedium
){
    Row(modifier = modifier
        .fillMaxWidth()
        .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically,
        ) {
        RadioButton(selected = selected, onClick =onClick)
        Text(text, style = textStyle)
    }
}