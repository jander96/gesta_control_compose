package com.devj.gestantescontrolcompose.ui.screens.homescreen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.devj.gestantescontrolcompose.R
import com.devj.gestantescontrolcompose.ui.model.PregnantUI
import com.devj.gestantescontrolcompose.ui.screens.common.AvatarImage
import com.devj.gestantescontrolcompose.utils.convertToBitmap

@Composable
fun RecyclerItem(modifier: Modifier = Modifier,pregnant: PregnantUI, onClick: (PregnantUI)-> Unit) {
    Card(
        colors=CardDefaults.cardColors(containerColor = Color(0xFFE5A6C4)),
        elevation=CardDefaults.cardElevation(10.dp),
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clip(RoundedCornerShape(topEnd = 10.dp, bottomEnd = 10.dp, topStart = 60.dp, bottomStart = 60.dp))
            .clickable {
                onClick(pregnant)
            }) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
                .fillMaxWidth()) {
            AvatarImage(size= 72.dp,image =if (pregnant.photo.isNotEmpty()) pregnant.photo.convertToBitmap() else null, placeholder = R.drawable.profile)
            Row(horizontalArrangement = Arrangement.Center,modifier = modifier
                .fillMaxWidth()) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = modifier
                        .fillMaxWidth()
                        .wrapContentHeight(),

                ) {
                    Text(
                        "${pregnant.name}. ",
                        overflow = TextOverflow.Ellipsis,
                        modifier = modifier.padding(bottom = 8.dp)

                    )
                    Text(
                        "${pregnant.gestationalAgeByFUM} semanas",
                        style = TextStyle.Default.copy(fontWeight = FontWeight.Bold)
                    )
                }
            }
        }
    }
}


