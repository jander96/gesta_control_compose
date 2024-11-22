package com.devj.gestantescontrolcompose.features.scheduler.domain

import kotlinx.coroutines.flow.Flow

interface MessageRepository {

    fun getAllMessage(): Flow<List<Message>>

    suspend fun getMessageById(messageId: Int): Message

    suspend fun insertMessage(message: Message)

    suspend fun deleteMessage(message: Message)

    suspend fun deleteMessageById(messageId: Int)

    fun search(query: String):Flow<List<Message>>
}