package com.devj.gestantescontrolcompose.common.presenter.composables

import androidx.annotation.DrawableRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.devj.gestantescontrolcompose.R

@Composable
fun ImageSelectorRow(
    @DrawableRes cameraIcon: Int = R.drawable.ic_camera,
    @DrawableRes galleryIcon: Int = R.drawable.ic_photo_library,
    modifier: Modifier = Modifier,
    onCameraClick: () -> Unit,
    onGalleryClick: () -> Unit,
) {
    Row(modifier = modifier) {
        Icon(
            painter = painterResource(cameraIcon),
            tint = Color.White,
            contentDescription = "camera",
            modifier = modifier
                .padding(8.dp)
                .clickable(onClick = onCameraClick)
        )
        Icon(
            painter = painterResource(galleryIcon),
            tint = Color.White,
            contentDescription = "camera",
            modifier = modifier
                .padding(8.dp)
                .clickable(onClick = onGalleryClick)
        )
    }
}
