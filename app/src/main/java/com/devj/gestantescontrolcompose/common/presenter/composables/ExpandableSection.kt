package com.devj.gestantescontrolcompose.common.presenter.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.devj.gestantescontrolcompose.common.extensions.Spacer16
import com.devj.gestantescontrolcompose.common.extensions.Spacer4
import com.devj.gestantescontrolcompose.common.extensions.SpacerBy


@Composable
fun ExpandableSection(
    modifier: Modifier = Modifier,
    leading: @Composable () -> Unit,
    content: @Composable () -> Unit,
    text: String,
    textStyle: TextStyle  = MaterialTheme.typography.titleSmall,
    contentPadding : Dp = 4.dp,
    shape: Shape = CardDefaults.shape,
    colors: CardColors = CardDefaults.cardColors(),
    elevation: CardElevation = CardDefaults.cardElevation(),
    border: BorderStroke? = null,
) {

    var isCalendarGroupExpand by rememberSaveable() { mutableStateOf(false) }
    val icon =
        if (isCalendarGroupExpand) Icons.Default.KeyboardArrowUp
        else Icons.Default.KeyboardArrowDown

    Card(
        modifier = modifier,
        shape = shape,
        colors = colors,
        elevation = elevation,
        border = border
    ) {
        Column(modifier = Modifier.padding(contentPadding)) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        isCalendarGroupExpand = !isCalendarGroupExpand
                    }
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    leading()
                    Spacer4()
                    Text(text, style = textStyle)
                }

                IconButton(onClick = {
                    isCalendarGroupExpand = !isCalendarGroupExpand
                }) {
                    Icon(imageVector = icon, contentDescription = null)
                }
            }

            AnimatedVisibility(isCalendarGroupExpand) {
                Spacer16()
                Divider(modifier = Modifier.padding(horizontal = 8.dp))
                SpacerBy(80)
                content()

            }
        }
    }


}