package com.devj.gestantescontrolcompose.common.service.work_manager

import android.content.Context
import androidx.work.WorkManager
import com.devj.gestantescontrolcompose.common.domain.SMSCancel
import com.devj.gestantescontrolcompose.features.scheduler.domain.Message
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class CancelSMSImpl @Inject constructor(
    @ApplicationContext private val context: Context
): SMSCancel{
    override fun cancel(message: Message) {
        val workManager = WorkManager.getInstance(context)
        workManager.cancelUniqueWork(message.id)
    }
}