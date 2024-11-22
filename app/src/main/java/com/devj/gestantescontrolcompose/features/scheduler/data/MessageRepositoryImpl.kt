package com.devj.gestantescontrolcompose.features.scheduler.data

import com.devj.gestantescontrolcompose.features.scheduler.data.cache.dao.MessageDao
import com.devj.gestantescontrolcompose.features.scheduler.data.cache.model.MessageEntity
import com.devj.gestantescontrolcompose.features.scheduler.domain.Message
import com.devj.gestantescontrolcompose.features.scheduler.domain.MessageRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MessageRepositoryImpl @Inject constructor(
    private val messageDao: MessageDao
): MessageRepository {
    override fun getAllMessage(): Flow<List<Message>> {
      return   messageDao.getAllMessage().map{list->
          list.map{it.toDomain()}
      }
    }

    override suspend fun getMessageById(messageId: Int): Message {
       return messageDao.getMessageById(messageId).toDomain()
    }

    override suspend fun insertMessage(message: Message) {
        messageDao.insertMessage(MessageEntity.fromDomain(message))
    }

    override suspend fun deleteMessage(message: Message) {
        messageDao.deleteMessage(MessageEntity.fromDomain(message))
    }

    override suspend fun deleteMessageById(messageId: Int) {
        messageDao.deleteMessageById(messageId)
    }

    override fun search(query: String): Flow<List<Message>> {
       return messageDao.search(query).map{list->
           list.map{it.toDomain()}
       }
    }

}