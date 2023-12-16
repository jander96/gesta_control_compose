package com.devj.gestantescontrolcompose.features.scheduler.presenter.views.screen

import android.content.Context
import android.content.Intent
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.devj.gestantescontrolcompose.common.extensions.toDateString
import com.devj.gestantescontrolcompose.features.home.ui.homescreen.FAB
import com.devj.gestantescontrolcompose.features.scheduler.domain.Message
import com.devj.gestantescontrolcompose.features.scheduler.presenter.views.composables.MessageItem
import com.devj.gestantescontrolcompose.features.scheduler.presenter.views.composables.ScheduleHeader
import com.devj.gestantescontrolcompose.features.scheduler.presenter.views.viewmodel.SchedulerViewModel
import java.time.LocalDateTime

@Composable
fun MessageSchedulePage(
    viewModel: SchedulerViewModel = hiltViewModel()
){
    val viewState by viewModel.viewState.collectAsStateWithLifecycle()
    val pregnantList by viewState.pregnantList.collectAsStateWithLifecycle(emptyList())
    val dateNow = LocalDateTime.now().toDateString()
    val scrollState = rememberLazyListState()

    val context = LocalContext.current
    var send by remember {
        mutableStateOf(false)
    }

    val smsPermission = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()){ granted ->
        if(granted){
           send = true
        }
    }
    Box() {
        
        Column(modifier = Modifier.padding(16.dp)) {
            ScheduleHeader(
                modifier = Modifier,
                date = dateNow,
                messageQuantity = 6,
                currentCost = 27.0,
                accumulatedCost = 100.0,
                totalCost = 127.0,
                username = "Ignacio Jimenez"
            )

            LazyColumn(modifier = Modifier, state = scrollState){
                items(pregnantList){pregnant->
                    MessageItem(
                        message = Message(
                            1,
                            "58800673",
                            "Por favor puedes venir a la consulta que te toca revision del segundo trimestre",
                            "2023-12-20 09:58",
                            "consulta"
                        ),
                        recipientList = pregnantList,
                        onDelete = {},
                        onClick = {},
                        onEdit = {}
                    )
                }
            }
        }
        AnimatedVisibility(
            scrollState.isScrollInProgress.not() ,
            enter = slideInVertically(
                initialOffsetY = { fullHeight -> -fullHeight },
                animationSpec = tween(durationMillis = 150, easing = LinearOutSlowInEasing)
            ),
            exit  = slideOutVertically(
                targetOffsetY = { fullHeight -> fullHeight },
                animationSpec = tween(durationMillis = 250, easing = FastOutLinearInEasing)
            ),
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 56.dp, end = 23.dp),
        ) {
            FAB(onClick = { })
        }
    }
}

@Preview
@Composable
fun SchedulerPagePreview() {
//    MessageSchedulePage()
}

//Box {
//    Column {
//        Text(text = "Programador de mensajes")
//
//        Button(onClick = {
//
//
//            smsPermission.launch(Manifest.permission.SEND_SMS)
//
//            val dateTime = LocalDateTime.now().plusMinutes(1)
//            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
//            val formattedDateTime = dateTime.format(formatter)
//
//            val scheduledSMS = ScheduledSMS(
//                context = context,
//                message = Message(
//                    1,
//                    "5351884229",
//                    "Esto es un mensaje de prueba GestaControl",
//                    formattedDateTime,
//                    "testMessage",
//                ),
//            )
//            if(send){
//                scheduledSMS.continuation.enqueue()
//            }
//
//
//        }) {
//            Text(text = "Test SMS")
//        }
//
//        Button(onClick = {
//            openBatterySettings(context)
//        }) {
//            Text(text = "Ir a Ajustes de energia")
//        }
//    }
//
//}

fun openBatterySettings(context: Context) {
    val intent = Intent(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS)
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    startActivity(context,intent,null)
}