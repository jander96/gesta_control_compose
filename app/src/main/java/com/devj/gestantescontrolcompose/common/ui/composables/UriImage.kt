package com.devj.gestantescontrolcompose.common.ui.composables

import android.graphics.Bitmap
import android.net.Uri
import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.devj.gestantescontrolcompose.common.extensions.getBitmap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun UriImage(
    imageUri: Uri?,
    modifier: Modifier = Modifier,
    @DrawableRes placeholder: Int,
    size: Dp = 120.dp,
    shape: Shape = CircleShape,
    contentScale: ContentScale = ContentScale.Crop
) {
    val context = LocalContext.current
    var imageBitmap: Bitmap? by remember {
        mutableStateOf(null)
    }
    LaunchedEffect(imageUri ){
        imageUri?.let {
            launch(Dispatchers.Default) {
            imageBitmap = context.getBitmap(imageUri)
            }
        }
    }
    AnimatedContent(targetState = imageBitmap, label = "bitmap_animation") {
        if (it != null) {
            Image(
                bitmap = imageBitmap!!.asImageBitmap(),
                contentDescription = "pregnant image",
                modifier = modifier
                    .clip(shape)
                    .size(size),
                contentScale = contentScale
            )
        } else {
            Image(
                painter = painterResource(placeholder),
                contentDescription = "pregnant image",
                modifier = modifier
                    .clip(shape)
                    .size(size),
                contentScale = contentScale
            )
        }
    }

}