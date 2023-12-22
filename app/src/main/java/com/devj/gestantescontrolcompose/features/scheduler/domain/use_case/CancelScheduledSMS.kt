package com.devj.gestantescontrolcompose.features.scheduler.domain.use_case

import com.devj.gestantescontrolcompose.common.domain.SMSCancel
import com.devj.gestantescontrolcompose.features.scheduler.domain.Message
import com.devj.gestantescontrolcompose.features.scheduler.domain.MessageRepository
import javax.inject.Inject

class CancelScheduledSMS @Inject constructor(
    private val messageRepo: MessageRepository,
    private val cancelSMS: SMSCancel,
) {
    suspend operator fun invoke(message: Message): Result<Unit> = runCatching {
        cancelSMS.cancel(message)
        messageRepo.deleteMessage(message)
    }
}