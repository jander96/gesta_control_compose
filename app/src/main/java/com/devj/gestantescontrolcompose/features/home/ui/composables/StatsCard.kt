package com.devj.gestantescontrolcompose.features.home.ui.composables


import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun StatCard(
    modifier: Modifier = Modifier,
    cardColor: Color = MaterialTheme.colorScheme.tertiaryContainer,
    size: Dp = 64.dp,
    value: Int,
    label: String,
    elevation : Dp = 2.dp
){

    val animatedValue = animateIntAsState(
        targetValue = value,
        label = "value_animation",
        animationSpec = tween(200)
    )
    Surface(
        modifier = modifier.size(size),
        color = cardColor,
        shape = MaterialTheme.shapes.small.copy(CornerSize(30f)),
        shadowElevation = elevation
    ) {
        Box {
            Text(
                "${animatedValue.value}",
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Light),
                modifier = Modifier.align(Alignment.Center)
            )
            Text(
                label,
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(4.dp),
            )
        }
    }
}