package com.devj.gestantescontrolcompose.features.scheduler.presenter.framework.work_manager

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.devj.gestantescontrolcompose.common.service.sms_service.SMS

class SMSWorker(
    private val context: Context,
    private val workerParams: WorkerParameters,
) : Worker(context, workerParams) {
    override fun doWork(): Result {
        val message = inputData.getString(Constants.KEY_MESSAGE) ?: return Result.failure()
        val phone = inputData.getString(Constants.KEY_PHONE) ?: return Result.failure()

        return sendMessage(phone, message)
    }

    private fun sendMessage(phone: String, message: String): Result {
        return try {
            val smsManager = SMS(context)
            smsManager.send(phone, message)
            Result.success()

        } catch (e: Exception) {
            print(e.message)
            Result.failure()
        }
    }
}