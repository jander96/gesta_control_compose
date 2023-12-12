package com.devj.gestantescontrolcompose.features.home.ui.composables

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.devj.gestantescontrolcompose.R
import com.devj.gestantescontrolcompose.common.extensions.Spacer16
import com.devj.gestantescontrolcompose.common.extensions.Spacer4
import com.devj.gestantescontrolcompose.common.extensions.convertToBitmap
import com.devj.gestantescontrolcompose.common.extensions.getIMClassification
import com.devj.gestantescontrolcompose.common.ui.composables.AvatarImage
import com.devj.gestantescontrolcompose.common.ui.composables.CircularIndicator
import com.devj.gestantescontrolcompose.common.ui.model.PregnantUI

@Composable
fun RecyclerItem(
    modifier: Modifier = Modifier,
    pregnant: PregnantUI,
    onClick: (PregnantUI) -> Unit,
    onDelete: (PregnantUI) -> Unit
) {
    var isExpanded by rememberSaveable {
        mutableStateOf(false)
    }
    val expandedIcon =
        if (!isExpanded) Icons.Default.KeyboardArrowDown else Icons.Default.KeyboardArrowUp

    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
        elevation = CardDefaults.cardElevation(1.dp),
        modifier = modifier
            .wrapContentHeight()
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 4.dp)
            .clickable { onClick(pregnant) },
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
                AvatarImage(
                    size = 48.dp,
                    image = if (pregnant.photo.isNotEmpty()) pregnant.photo.convertToBitmap() else null,
                    placeholder = R.drawable.woman_avatar,
                )

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
                    pregnant.name,
                    overflow = TextOverflow.Ellipsis,

                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier.width(6.dp))
                if (pregnant.lastName.isBlank().not()) {
                    Text(
                        pregnant.lastName,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }

            Spacer16()
            Row(modifier = modifier
                .fillMaxWidth()
                .padding(8.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(R.drawable.ic_baby_boy),
                        contentDescription = "FPP",
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer4()
                    Surface(
                        color = MaterialTheme.colorScheme.onPrimary,
                        shape = MaterialTheme.shapes.small.copy(CornerSize(15.dp)),
                        tonalElevation = 1.dp,
                        modifier = Modifier.padding(horizontal = 4.dp)
                    ){
                        Text(
                            pregnant.fpp,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        )
                    }

                }
                Surface(
                    color = MaterialTheme.colorScheme.onPrimary,
                    shape = MaterialTheme.shapes.small.copy(CornerSize(15.dp)),
                    tonalElevation = 1.dp,
                    modifier = Modifier.padding(horizontal = 4.dp)
                ) {
                    Text(
                        "${
                            if (pregnant.isFUMReliable) pregnant.gestationalAgeByFUM
                            else pregnant.gestationalAgeByFirstUS
                        } sem",
                        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            AnimatedContent(isExpanded, label = "Expand content") { expandable ->
                if (expandable) {
//
                    Row(
                        modifier = modifier
                            .padding(8.dp)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                        ) {

                            CircularIndicator(
                                smallText = "IMC",
                                indicatorValue = pregnant.iMC.toFloat(),
                                maxIndicatorValue = 40f,
                                canvasSize = 56.dp,
                            )
                            Spacer4()
                            Text(
                                pregnant.iMC.toDouble().getIMClassification(),
                                style = MaterialTheme.typography.bodySmall
                            )
                        }

                        IconButton(
                            colors = IconButtonDefaults.iconButtonColors(
                                containerColor = MaterialTheme.colorScheme.errorContainer,
                            ),
                            onClick = {
                                onDelete(pregnant)
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



