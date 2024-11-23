package com.devj.gestantescontrolcompose.common.service.work_manager

import android.content.Context
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.ListenableWorker
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.OutOfQuotaPolicy
import androidx.work.WorkContinuation
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.devj.gestantescontrolcompose.common.domain.SMSScheduler
import com.devj.gestantescontrolcompose.features.scheduler.domain.Message
import dagger.hilt.android.qualifiers.ApplicationContext
import java.time.Duration
import java.time.LocalDateTime
import java.time.ZonedDateTime
import javax.inject.Inject

object Constants {
    const val KEY_MESSAGE = "sms_message"
    const val KEY_PHONE = "phone"
}

/**
 * Schedule a sms
 */
class SMSSchedulerImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    ): SMSScheduler {

    override fun schedule(message: Message) {
        message.addressees.forEach { phoneNumber ->

            val date = message.dateTime?.plusSeconds(15)

            val inputMessage: Data = workDataOf(
                Constants.KEY_MESSAGE to message.message,
                Constants.KEY_PHONE to phoneNumber,
            )


            val smsWork = OneTimeWorkRequestBuilder<SMSWorker>()
                .setInputData(inputMessage)
                .setInitialDelay(calculateDelayByDateTime(date))
                .addTag(message.tag)
                .build()

            WorkManager.getInstance(context)
                .enqueueUniqueWork(message.tag, ExistingWorkPolicy.REPLACE, smsWork)

        }
    }

    /**
     * Generate a [Duration] from the difference between current time
     * and [dateTime] pass as parameter
     */
    private fun calculateDelayByDateTime(dateTime: ZonedDateTime?): Duration {
        val now = LocalDateTime.now()
        return Duration.between(now, dateTime)
    }

    /**
     * Applies a [ListenableWorker] to a [WorkContinuation] in case [apply] is `true`.
     */
    private inline fun <reified T : ListenableWorker> WorkContinuation.thenMaybe(
        apply: Boolean, inputData: Data ,
    ): WorkContinuation {
        return if (apply) {
            then(workRequest<T>(inputData))
        } else {
            this
        }
    }

    /**
     * Creates a [OneTimeWorkRequest] with the given inputData and a [tag] if set.
     */
    private inline fun <reified T : ListenableWorker> workRequest(
        inputData: Data ,
        tag: String? = null
    ) =
        OneTimeWorkRequestBuilder<T>().apply {
            setInputData(inputData)
            setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
            if (!tag.isNullOrEmpty()) {
                addTag(tag)
            }
        }.build()


}