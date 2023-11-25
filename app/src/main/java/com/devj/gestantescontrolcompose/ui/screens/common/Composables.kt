package com.devj.gestantescontrolcompose.ui.screens.common

import android.graphics.Bitmap
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.devj.gestantescontrolcompose.R

@Composable
fun AvatarImage(
    size: Dp = 120.dp,
    image: Bitmap? ,
    @DrawableRes placeholder: Int,
    modifier: Modifier = Modifier){
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

@Composable
fun ImageSelectorRow(
    @DrawableRes cameraIcon: Int = R.drawable.ic_camera,
    @DrawableRes galleryIcon: Int = R.drawable.ic_photo_library,
    modifier: Modifier = Modifier,
    onCameraClick: ()->Unit,
    onGalleryClick: ()->Unit,){
    Row(modifier = modifier) {
        Icon(painter = painterResource(cameraIcon),
            tint = Color.White,
            contentDescription = "camera",
            modifier = modifier
                .padding(8.dp)
                .clickable(onClick = onCameraClick))
        Icon(painter = painterResource(galleryIcon),
            tint = Color.White,
            contentDescription = "camera",
            modifier = modifier
                .padding(8.dp)
                .clickable(onClick = onGalleryClick))
    }
}