package com.devj.gestantescontrolcompose.features.scheduler.domain.use_case

import com.devj.gestantescontrolcompose.common.domain.SMSScheduler
import com.devj.gestantescontrolcompose.features.scheduler.domain.Message
import com.devj.gestantescontrolcompose.features.scheduler.domain.MessageRepository
import javax.inject.Inject

class ScheduleMessage @Inject constructor(
    private val messageRepo: MessageRepository,
    private val smsScheduler: SMSScheduler,
) {
    suspend operator fun invoke(message: Message): Result<Unit> = runCatching {
        smsScheduler.schedule(message)
        messageRepo.insertMessage(message)
    }
}