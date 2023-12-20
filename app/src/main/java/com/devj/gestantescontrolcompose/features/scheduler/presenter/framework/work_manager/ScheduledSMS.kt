package com.devj.gestantescontrolcompose.features.scheduler.presenter.framework.work_manager

import android.annotation.SuppressLint
import android.content.Context
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.devj.gestantescontrolcompose.common.utils.DateTimeHelper
import com.devj.gestantescontrolcompose.features.scheduler.domain.Message
import java.time.Duration
import java.time.LocalDateTime

object Constants {
    const val KEY_MESSAGE = "sms_message"
    const val KEY_PHONE = "phone"
}

/**
 * Builds and holds WorkContinuation based on supplied filters.
 */

@SuppressLint("EnqueueWork")
class ScheduledSMS(
    context: Context,
    message: Message,
) {

    init {
        message.addressees.forEach { phoneNumber ->
            val formatter = DateTimeHelper.fullDateTimeAmFormatter
            val date = LocalDateTime.parse(message.dateTime, formatter).plusSeconds(15)

            val inputMessage: Data = workDataOf(
                Constants.KEY_MESSAGE to message.message,
                Constants.KEY_PHONE to phoneNumber,
            )

            val smsWork = OneTimeWorkRequestBuilder<SMSWorker>()
                .setInputData(inputMessage)
                .setInitialDelay(calculateDelayByDateTime(date))
                .addTag(message.tag)
                .build()
            WorkManager.getInstance(context).enqueue(smsWork)

        }

    }

    /**
     * Generate a [Duration] from the difference between current time
     * and [dateTime] pass as parameter
     */
    private fun calculateDelayByDateTime(dateTime: LocalDateTime): Duration {
        val now = LocalDateTime.now()
        return Duration.between(now, dateTime)
    }

//    /**
//     * Applies a [ListenableWorker] to a [WorkContinuation] in case [apply] is `true`.
//     */
//    private inline fun <reified T : ListenableWorker> WorkContinuation.thenMaybe(
//        apply: Boolean
//    ): WorkContinuation {
//        return if (apply) {
//            then(workRequest<T>())
//        } else {
//            this
//        }
//    }
//
//    /**
//     * Creates a [OneTimeWorkRequest] with the given inputData and a [tag] if set.
//     */
//    private inline fun <reified T : ListenableWorker> workRequest(
//        inputData: Data = inputMessage,
//        tag: String? = null
//    ) =
//        OneTimeWorkRequestBuilder<T>().apply {
//            setInputData(inputData)
//            setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
//            if (!tag.isNullOrEmpty()) {
//                addTag(tag)
//            }
//        }.build()
}