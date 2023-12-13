package com.devj.gestantescontrolcompose.features.scheduler.view.message_schedulescreen

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import com.devj.gestantescontrolcompose.common.domain.model.Message
import com.devj.gestantescontrolcompose.common.service.sms_service.ScheduledSMS
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun MessageSchedulePage(){

    val context = LocalContext.current
    var send by remember {
        mutableStateOf(false)
    }

    val smsPermission = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()){ granted ->
        if(granted){
           send = true
        }
    }


    Box {
        Text(text = "Programador de mensajes")

        Button(onClick = {


            smsPermission.launch(Manifest.permission.SEND_SMS)

            val dateTime = LocalDateTime.now().plusMinutes(1)
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            val formattedDateTime = dateTime.format(formatter)

            val scheduledSMS = ScheduledSMS(
                tag = "testMessage",
                context =context,
                message = Message("5351884229","Esto es un mensaje de prueba GestaControl"),
                dateTime = formattedDateTime
            )
            if(send){
                scheduledSMS.continuation.enqueue()
            }


        }) {
            Text(text = "Test SMS")
        }
    }
}