package com.devj.gestantescontrolcompose.common.presenter.composables


import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp

@Composable
fun CircularIndicator(
    modifier: Modifier = Modifier,
    canvasSize: Dp = 300.dp,
    indicatorValue: Float = 0f,
    maxIndicatorValue: Float = 100f,
    backgroundIndicatorColor: Color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f),
    backgroundIndicatorStrokeWidth: Float = canvasSize.value * 0.2f,
    foregroundIndicatorColors: List<Color> = listOf(
        Color(0xFFD0D383),
        Color(0xFFAFB42B),
        Color(0xFF0097A7),
        Color(0xFFF57C00),
        ),
    foregroundIndicatorStrokeWidth: Float = canvasSize.value * 0.2f,
    indicatorStrokeCap: StrokeCap = StrokeCap.Round,
    bigTextFontSize: TextUnit = TextUnit(value = canvasSize.value * 0.20f, type = TextUnitType.Sp),
    bigTextColor: Color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
    bigTextSuffix: String = "",
    smallText: String = "",
    smallTextColor: Color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
    smallTextFontSize: TextUnit =  TextUnit(value = canvasSize.value * 0.16f, type = TextUnitType.Sp),
    bigTextStyle: TextStyle = MaterialTheme.typography.titleMedium,
    smallTextStyle: TextStyle = MaterialTheme.typography.titleMedium,
    hasTop: Boolean = false
) {


    var allowedIndicatorValue by remember { mutableFloatStateOf(maxIndicatorValue)}

    allowedIndicatorValue = if (hasTop) {
        if (indicatorValue <= maxIndicatorValue) indicatorValue
        else maxIndicatorValue
    } else {
        indicatorValue
    }

    var animatedIndicatorValue by remember{ mutableFloatStateOf(0f) }

    LaunchedEffect(allowedIndicatorValue){
        animatedIndicatorValue =
            if (allowedIndicatorValue < maxIndicatorValue) allowedIndicatorValue
            else maxIndicatorValue
    }

    val percentage = (animatedIndicatorValue / maxIndicatorValue) * 100
    val sweepAngle by animateFloatAsState(
        targetValue = (2.4 * percentage).toFloat(),
        label = "sweepAngle",
        animationSpec = tween(1000)
        )

//    val receivedValue by animateFloatAsState(
//        targetValue = allowedIndicatorValue,
//        animationSpec = tween(1000),
//        label = "valueAnimation"
//    )

    val animatedTextColor by animateColorAsState(
        targetValue = if (allowedIndicatorValue == 0f) MaterialTheme.colorScheme.onSurface.copy(
            alpha = 0.3f
        ) else bigTextColor,
        animationSpec = tween(1000),
        label = "colorAnimation"
    )
    val animatedForegroundColor by animateColorAsState(
        targetValue =
        if (foregroundIndicatorColors.size < 4)
            MaterialTheme.colorScheme.primary
        else when (percentage) {
            in 0f..24f -> foregroundIndicatorColors.first()
            in 25f..50f -> foregroundIndicatorColors[1]
            in 51f..75f -> foregroundIndicatorColors[2]
            in 76f..100f -> foregroundIndicatorColors.last()
            else -> foregroundIndicatorColors.last()
        },
        animationSpec = tween(1000),
        label = "colorAnimation"
    )
    Column(
        modifier = modifier
            .size(canvasSize)
            .drawBehind {
                val componentSize = size / 1.25f
                backgroundIndicator(
                    componentSize = componentSize,
                    indicatorColor = backgroundIndicatorColor,
                    indicatorStrokeWidth = backgroundIndicatorStrokeWidth,
                    indicatorStrokeCap = indicatorStrokeCap,
                )
                foregroundIndicator(
                    sweepAngle = sweepAngle,
                    componentSize = componentSize,
                    indicatorColor = animatedForegroundColor,
                    indicatorStrokeWidth = foregroundIndicatorStrokeWidth,
                    indicatorStrokeCap = indicatorStrokeCap,
                )
            },
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        EmbeddedElements(
            bigText = allowedIndicatorValue,
            bigTextFontSize = bigTextFontSize,
            bigTextColor = animatedTextColor,
            bigTextSuffix = bigTextSuffix,
            smallText = smallText,
            smallTextColor = smallTextColor,
            smallTextFontSize = smallTextFontSize,
            bigTextStyle  = bigTextStyle,
            smallTextStyle = smallTextStyle,
        )

    }
}

fun DrawScope.backgroundIndicator(
    componentSize: Size,
    indicatorColor: Color,
    indicatorStrokeWidth: Float,
    indicatorStrokeCap: StrokeCap
){
    drawArc(
        size = componentSize,
        color = indicatorColor,
        startAngle = 150f,
        sweepAngle = 240f,
        useCenter = false,
        style = Stroke(
            width = indicatorStrokeWidth,
            cap = indicatorStrokeCap
        ),
        topLeft = Offset(
            x =  (size.width - componentSize.width) * 0.5f,
            y = (size.height - componentSize.height) * 0.5f
        )
    )
}

fun DrawScope.foregroundIndicator(
    sweepAngle: Float,
    componentSize: Size,
    indicatorColor: Color,
    indicatorStrokeWidth: Float,
    indicatorStrokeCap: StrokeCap
){
    drawArc(
        size = componentSize,
        color = indicatorColor,
        startAngle = 150f,
        sweepAngle = sweepAngle,
        useCenter = false,
        style = Stroke(
            width = indicatorStrokeWidth,
            cap = indicatorStrokeCap
        ),
        topLeft = Offset(
            x =  (size.width - componentSize.width) * 0.5f,
            y = (size.height - componentSize.height) * 0.5f
        )
    )
}

@Composable
fun EmbeddedElements(
    bigText: Float,
    bigTextFontSize: TextUnit,
    bigTextColor: Color,
    bigTextSuffix: String,
    smallText: String,
    smallTextColor: Color,
    smallTextFontSize: TextUnit,
    bigTextStyle: TextStyle,
    smallTextStyle: TextStyle,
){

    Text(
        text = smallText,
        style = smallTextStyle,
        color = smallTextColor,
        fontSize = smallTextFontSize,
        textAlign = TextAlign.Center
    )
    Text(
        text = if (bigTextSuffix.isNotEmpty())"$bigText $bigTextSuffix" else "$bigText",
        color = bigTextColor,
        style = bigTextStyle.copy(fontSize = bigTextFontSize),
        textAlign = TextAlign.Center,
        fontWeight = FontWeight.Bold
    )

}

@Composable
@Preview(showBackground = true)
fun CircularIndicatorPreview(){
    CircularIndicator(
        indicatorValue = 60f,
        smallText = "IMC"
    )
}