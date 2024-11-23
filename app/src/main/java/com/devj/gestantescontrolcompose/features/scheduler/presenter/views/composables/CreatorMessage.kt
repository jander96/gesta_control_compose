package com.devj.gestantescontrolcompose.features.scheduler.presenter.views.composables

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.devj.gestantescontrolcompose.R
import com.devj.gestantescontrolcompose.common.extensions.Spacer4
import com.devj.gestantescontrolcompose.common.utils.DateTimeHelper.toStandardDate
import com.devj.gestantescontrolcompose.common.utils.DateTimeHelper.toStandardTime
import com.devj.gestantescontrolcompose.features.scheduler.domain.Message
import java.time.LocalTime
import java.time.ZonedDateTime

@Composable
fun CreatorMessage(
    textMessage: String,
    onMessageChange: (String) -> Unit,
    onCalendarClick: () -> Unit,
    onClockClick: () -> Unit,
    onCleanClick: () -> Unit,
    onAddresseeClick: () -> Unit,
    onSendClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    message: Message? = null,
    date: ZonedDateTime? = null,
    time: LocalTime? = null,
    remitters: Int? = null,
    isValidMessage: Boolean = false,
) {
    var text by rememberSaveable {
        mutableStateOf("")
    }
    LaunchedEffect(key1 = message?.message){
        message?.message?.let {
            text = it
        }
    }
    text = textMessage
    Column(modifier = modifier) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            OutlinedTextField(
                placeholder = {
                    Text(text = "mensaje", style = MaterialTheme.typography.bodyMedium)
                },
                value = text,
                shape = MaterialTheme.shapes.small.copy(CornerSize(30.dp)),
                onValueChange = onMessageChange,
                modifier = Modifier.weight(3f),
                trailingIcon = {
                    IconButton(
                        onClick = {
                            val id = message?.id ?: ""
                            onSendClick(id)
                        },
                        enabled = isValidMessage,
                    ) {

                        Icon(
                            modifier = Modifier.size(24.dp),
                            painter = painterResource(id = R.drawable.ic_send_clock_svg),
                            contentDescription = "",
                        )
                    }
                }
            )

        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
            ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                ) {
                IconButton(onClick = onCalendarClick) {
                    Icon(
                        modifier = Modifier.size(24.dp),
                        painter = painterResource(id = R.drawable.ic_schedule_message_svg),
                        contentDescription = "",
                    )
                }

                Spacer4()
                IconButton(onClick = onClockClick) {
                    Icon(
                        modifier = Modifier.size(24.dp),
                        painter = painterResource(id = R.drawable.ic_clock_seven_svg),
                        contentDescription = "",
                    )
                }

                Spacer4()
                IconButton(onClick = onAddresseeClick) {
                    Icon(
                        modifier = Modifier.size(24.dp),
                        painter = painterResource(id = R.drawable.ic_contacts_svg),
                        contentDescription = "",
                    )
                }
            }


            IconButton(onClick = onCleanClick) {
                Icon(
                    modifier = Modifier.size(24.dp),
                    painter = painterResource(id = R.drawable.ic_cleaner_svg),
                    contentDescription = "clean_all" )
            }

        }
        Row(modifier = Modifier.padding(bottom = 8.dp)) {
            if (date != null)
                Label(text = date.toStandardDate())
            Spacer4()
            if (time !=null )
                Label(text = time.toStandardTime())
            Spacer4()
            if (remitters != null && remitters != 0)
                Label(text ="$remitters destinatarias")
        }
    }
}

@Composable
fun Label(modifier: Modifier = Modifier, text: String) {
    Surface(
        border = BorderStroke(0.5.dp, MaterialTheme.colorScheme.onSecondaryContainer),
        modifier = modifier,
        shape = MaterialTheme.shapes.small.copy(CornerSize(30.dp)),
        color = MaterialTheme.colorScheme.secondaryContainer
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}

@Preview
@Composable
fun CreatorMessagePreview() {
    CreatorMessage(
        message = null,
        textMessage = "Ven manaa a consulta",
        onMessageChange = {},
        onCalendarClick = {},
        onClockClick = {},
        onAddresseeClick = {},
        onSendClick = {},
        onCleanClick = {}
    )
}

