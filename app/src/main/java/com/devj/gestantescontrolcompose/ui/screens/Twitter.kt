package com.devj.gestantescontrolcompose.ui.screens


import androidx.compose.foundation.Image

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.devj.gestantescontrolcompose.R


@Composable
fun Twit(
    modifier: Modifier,
    onCommentClick: () -> Unit,
    onRtClick: () -> Unit,
    onLikeClick: () -> Unit,
    isEnable : Boolean
) {


    Column(modifier = modifier.padding(8.dp)) {
        Row() {
            Image(
                painter = painterResource(R.drawable.profile),
                contentDescription = "foto de perfil",
                modifier = modifier
                    .size(48.dp)
                    .clip(CircleShape)
            )

            Spacer(modifier.size(16.dp))
            Column() {
                Text("Jander Laffita", fontWeight = FontWeight.Bold)
                Text("Contenido del texto, esto es un twit de ejemplo para trabajar con compose")
                Image(
                    painter = painterResource(R.drawable.profile),
                    contentDescription = "foto de perfil",
                    modifier = modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .padding(vertical = 8.dp)
                        .clip(RoundedCornerShape(10)),
                    contentScale = ContentScale.Crop
                )

                Row(
                    horizontalArrangement = Arrangement.SpaceBetween, modifier = modifier
                        .fillMaxWidth()

                        .padding(horizontal = 16.dp)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_chat),
                        contentDescription = "chat",
                        modifier = modifier.clickable { onCommentClick() })
                    Icon(
                        painter = painterResource(R.drawable.ic_rt),
                        contentDescription = "retwit",
                        modifier = modifier.clickable { onRtClick() })
                    if (isEnable) {
                        Icon(
                            tint = Color.Red,
                            painter = painterResource(R.drawable.ic_like_filled),
                            contentDescription = "like",
                            modifier = modifier.clickable {
                                onLikeClick()

                            })
                    }
                    else {
                        Icon(
                            painter = painterResource(R.drawable.ic_like),
                            contentDescription = "like",
                            modifier = modifier.clickable { onLikeClick() })
                    }
                    Icon(
                        painter = painterResource(R.drawable.ic_share),
                        contentDescription = "like",
                        modifier = modifier.clickable {  })
                }
            }
        }


    }
}

@Preview(showSystemUi = false, showBackground = true)
@Composable
fun MyPreview() {
    Twit(modifier = Modifier, onCommentClick = {}, onRtClick = {}, onLikeClick = {},true
    )
}