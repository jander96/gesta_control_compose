package com.devj.gestantescontrolcompose.features.scheduler.domain.use_case

import com.devj.gestantescontrolcompose.features.scheduler.domain.Message
import com.devj.gestantescontrolcompose.features.scheduler.domain.MessageRepository
import javax.inject.Inject

class SaveMessage @Inject constructor(private val messageRepo: MessageRepository) {
    suspend operator fun invoke(message: Message): Result<Unit> = runCatching {
        messageRepo.insertMessage(message)
    }
}