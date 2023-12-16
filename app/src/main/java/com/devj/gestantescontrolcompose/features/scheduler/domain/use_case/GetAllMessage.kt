package com.devj.gestantescontrolcompose.features.scheduler.domain.use_case

import com.devj.gestantescontrolcompose.features.scheduler.domain.Message
import com.devj.gestantescontrolcompose.features.scheduler.domain.MessageRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllMessage @Inject constructor(private val messageRepo: MessageRepository) {
    operator fun invoke(): Result<Flow<List<Message>>> = runCatching {
        messageRepo.getAllMessage()
    }
}