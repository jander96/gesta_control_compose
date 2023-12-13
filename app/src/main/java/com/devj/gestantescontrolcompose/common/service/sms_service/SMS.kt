package com.devj.gestantescontrolcompose.common.service.sms_service

import android.content.Context
import android.telephony.SmsManager

class SMS(private val context: Context) {
    private val smsManager = SmsManager.getDefault()

    fun send(phoneNumber: String, message: String) {

        smsManager.sendTextMessage(
            phoneNumber,
            null,
            message,
            null,
            null,
        )

    }
}