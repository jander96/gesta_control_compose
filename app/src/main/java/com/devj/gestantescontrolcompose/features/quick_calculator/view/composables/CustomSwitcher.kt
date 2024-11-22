package com.devj.gestantescontrolcompose.features.quick_calculator.view.composables

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp


@Composable
fun CustomSwitcher(
    onText: String,
    offText: String,
    modifier: Modifier = Modifier,
    isOn: Boolean = false,
    size: Dp = 150.dp,
    fontSize: Dp = size / 3,
    padding: Dp = 10.dp,
    borderWidth: Dp = 1.dp,
    parentShape: Shape = CircleShape,
    toggleShape: Shape = CircleShape,
    animationSpec: AnimationSpec<Dp> = tween(durationMillis = 300),
    onClick: () -> Unit
) {
    val offset by animateDpAsState(
        targetValue = if (isOn) 0.dp else size * 1.35f,
        animationSpec = animationSpec,
        label = ""
    )

    Box(modifier = modifier
        .width(size * 2.5f)
        .height(size)
        .clip(shape = parentShape)
        .clickable { onClick() }
        .background(MaterialTheme.colorScheme.primaryContainer)
    ) {
        SwitchButton(
            size,
            offset,
            padding,
            modifier,
            toggleShape
        )

        Row(
            modifier = Modifier
                .fillMaxSize()
                .border(
                    border = BorderStroke(
                        width = borderWidth,
                        color = MaterialTheme.colorScheme.primary
                    ),
                    shape = parentShape
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween

        ) {

                Text(
                    onText,
                    modifier = Modifier.padding(padding),
                    style = MaterialTheme.typography.bodySmall,
                    color = if (isOn) MaterialTheme.colorScheme.onPrimary
                    else MaterialTheme.colorScheme.primary
                )


                Text(
                    text = offText,
                    modifier = Modifier.padding(padding),
                    style = MaterialTheme.typography.bodySmall,
                    color = if (isOn) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.onPrimary
                )

        }
    }
}

@Composable
fun SwitchButton(
    size: Dp,
    offset: Dp,
    padding: Dp,
    modifier: Modifier = Modifier,
    toggleShape: Shape = CircleShape,
    background: Color = MaterialTheme.colorScheme.primary
){
    Surface(
        modifier = modifier
            .size(size * 1.2f)
            .offset(x = offset)
            .padding(all = padding)
            .clip(shape = toggleShape),
        tonalElevation = 6.dp,
        shadowElevation = 6.dp,
        color = background
    ) {
    }
}

@Preview()
@Composable
fun CustomSwitcherPreview(){
    CustomSwitcher(
        onText = "FUM",
        offText = "USG"
    ){

    }
}