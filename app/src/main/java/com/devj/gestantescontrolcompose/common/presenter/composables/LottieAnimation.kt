package com.devj.gestantescontrolcompose.common.presenter.composables

import androidx.annotation.RawRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition

@Composable
fun InfiniteLottieAnimation(
    @RawRes rawRes: Int,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Fit
    ){
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(rawRes))
    val progress by animateLottieCompositionAsState(
        composition,
        iterations = LottieConstants.IterateForever
    )
    LottieAnimation(
        modifier  = modifier,
        composition = composition,
        progress = { progress },
        contentScale = contentScale
    )
}