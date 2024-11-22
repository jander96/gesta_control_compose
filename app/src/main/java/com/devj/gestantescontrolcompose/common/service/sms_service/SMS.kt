package com.devj.gestantescontrolcompose.common.service.sms_service

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.telephony.SmsManager
import android.telephony.TelephonyManager

class SMS(private val context: Context) {
// this is working but deprecated
//    private val smsManager = SmsManager.getDefault()

    private val smsManager: SmsManager = context.getSystemService(SmsManager::class.java)

    fun send(phoneNumber: String, message: String) {
        val sentPI = PendingIntent.getBroadcast(context, 0,
            Intent("SMS_SENT"), PendingIntent.FLAG_IMMUTABLE)

        val deliveredPI = PendingIntent.getBroadcast(context, 0,
            Intent("SMS_DELIVERED"),  PendingIntent.FLAG_IMMUTABLE)

        smsManager.sendTextMessage(
            phoneNumber,
            null,
            message,
            sentPI,
            deliveredPI,
        )

    }

}