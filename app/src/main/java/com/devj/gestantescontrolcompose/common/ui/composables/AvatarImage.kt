package com.devj.gestantescontrolcompose.common.ui.composables

import android.graphics.Bitmap
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun AvatarImage(
    size: Dp = 120.dp,
    image: Bitmap?,
    @DrawableRes placeholder: Int,
    modifier: Modifier = Modifier
) {
    if (image != null) {
        Image(
            bitmap = image.asImageBitmap(),
            contentDescription = "pregnant image",
            modifier = modifier
                .clip(CircleShape)
                .size(size),
            contentScale = ContentScale.Crop
        )
    } else {
        Image(
            painter = painterResource(placeholder),
            contentDescription = "pregnant image",
            modifier = modifier
                .clip(CircleShape)
                .size(size),
            contentScale = ContentScale.Crop
        )
    }
}