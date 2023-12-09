package com.devj.gestantescontrolcompose.features.quick_calculator.extensions

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally

fun enterFromEnd(): EnterTransition{
    return  scaleIn(
        animationSpec = tween(800, 100),
    ) + slideInHorizontally(
        animationSpec = tween(800, 100),
        initialOffsetX = {width-> width }
    ) + fadeIn(
        animationSpec = tween(800, 100),
        initialAlpha = 0.5f
    )
}

fun enterFromStart(): EnterTransition{
    return  scaleIn(
        animationSpec = tween(800, 100),
    ) + slideInHorizontally(
        animationSpec = tween(800, 100),
        initialOffsetX = {width-> -width }
    ) + fadeIn(
        animationSpec = tween(800, 100),
        initialAlpha = 0.5f
    )
}

fun exitToEnd(): ExitTransition{
   return  slideOutHorizontally(
        animationSpec = tween(800, 100),
        targetOffsetX = {width-> width }
    ) + fadeOut(
        animationSpec = tween(800, 100))
}

fun exitToStart(): ExitTransition{
    return  slideOutHorizontally(
        animationSpec = tween(800, 100),
        targetOffsetX = {width-> -width }
    ) + fadeOut(
        animationSpec = tween(800, 100))
}