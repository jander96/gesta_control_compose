package com.devj.gestantescontrolcompose.features.scheduler.presenter.views.composables

import android.net.Uri
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.devj.gestantescontrolcompose.R
import com.devj.gestantescontrolcompose.common.extensions.Spacer16
import com.devj.gestantescontrolcompose.common.extensions.Spacer4
import com.devj.gestantescontrolcompose.common.presenter.composables.UriImage
import com.devj.gestantescontrolcompose.common.presenter.model.PregnantUI
import com.devj.gestantescontrolcompose.common.utils.DateTimeHelper
import com.devj.gestantescontrolcompose.features.scheduler.domain.Message

@Composable
fun MessageItem(
    modifier: Modifier = Modifier,
    message: Message,
    recipientList: List<PregnantUI>,
    onClick: (Message) -> Unit,
    onDelete: (Message) -> Unit,
    onEdit: (Message) -> Unit
) {
    var isExpanded by rememberSaveable {
        mutableStateOf(false)
    }
    val expandedIcon =
        if (!isExpanded) Icons.Default.KeyboardArrowDown else Icons.Default.KeyboardArrowUp
    val color =  MaterialTheme.colorScheme.background

    Card(
        colors = CardDefaults.cardColors(containerColor = color ),
        elevation = CardDefaults.cardElevation(1.dp),
        modifier = modifier
            .wrapContentHeight()
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 4.dp)
            .clickable { onClick(message) },
        shape = MaterialTheme.shapes.medium,
    ) {
        Column(modifier = modifier.animateContentSize()) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = modifier
                    .padding(8.dp)
                    .fillMaxWidth()
            ) {
                Row(horizontalArrangement = Arrangement.Start) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            painter = painterResource(R.drawable.ic_timer_svg),
                            contentDescription = "FPP",
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer4()
                        Surface(
                            color = MaterialTheme.colorScheme.tertiaryContainer,
                            shape = MaterialTheme.shapes.small.copy(CornerSize(15.dp)),
                            tonalElevation = 1.dp,
                            modifier = Modifier.padding(horizontal = 4.dp)
                        ){
                            Text(
                                DateTimeHelper.ymdToHumanReadableDate(message.dateTime),
                                style = MaterialTheme.typography.bodySmall,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            )
                        }

                    }
                }


                Surface(
                    color = MaterialTheme.colorScheme.onPrimary,
                    shape = MaterialTheme.shapes.small.copy(CornerSize(50.dp)),
                    modifier = Modifier.size(40.dp),
                    tonalElevation = 1.dp,
                ) {
                    IconButton(onClick = {
                        isExpanded = !isExpanded
                    }) {
                        Icon(imageVector = expandedIcon, contentDescription = "expand")
                    }
                }

            }

            Row(
                modifier = modifier.padding(horizontal = 8.dp),
            ) {
                Text(
                    message.message,
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Justify
                )
            }
            Spacer16()
            AnimatedContent(isExpanded, label = "Expand content") { expandable ->

                if (expandable) {
                    Column {

                        Row(
                            modifier = modifier
                                .padding(8.dp)
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column() {
                                Text(
                                    text = stringResource(R.string.addressee),
                                    style = MaterialTheme.typography.labelMedium
                                )
                                LazyRow() {
                                    items(recipientList) { pregnant ->
                                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                            UriImage(
                                                modifier = Modifier.padding(4.dp),
                                                size = 40.dp,
                                                imageUri = if (pregnant.photo.isNotEmpty()) Uri.parse(
                                                    pregnant.photo
                                                ) else null,
                                                placeholder = R.drawable.woman_avatar,
                                            )
                                            Text(
                                                text = pregnant.name,
                                                style = MaterialTheme.typography.labelSmall
                                            )
                                        }
                                    }

                                }
                            }
                        }

                        Row(horizontalArrangement = Arrangement.End,modifier = Modifier.fillMaxWidth()) {
                            Spacer16()
                            IconButton(
                                colors = IconButtonDefaults.iconButtonColors(
                                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                                ),
                                onClick = {
                                    onEdit(message)
                                }) {
                                Icon(
                                    painter = painterResource(R.drawable.ic_edit_svg),
                                    contentDescription = "edit",
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                            Spacer4()
                            IconButton(
                                colors = IconButtonDefaults.iconButtonColors(
                                    containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                                ),
                                onClick = {
                                    onDelete(message)
                                }) {
                                Icon(
                                    painter = painterResource(R.drawable.ic_trash_svg),
                                    contentDescription = "delete",
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }
                    }


                }

            }
        }
    }

}



