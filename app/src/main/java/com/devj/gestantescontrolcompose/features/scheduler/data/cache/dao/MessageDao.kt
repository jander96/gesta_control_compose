package com.devj.gestantescontrolcompose.features.scheduler.data.cache.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.devj.gestantescontrolcompose.features.scheduler.data.cache.model.MessageEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MessageDao {
    @Query("SELECT * FROM messages_table")
    fun getAllMessage(): Flow<List<MessageEntity>>

    @Query("SELECT * FROM messages_table WHERE id = :messageId")
    suspend fun getMessageById(messageId: Int): MessageEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE )
    suspend fun insertMessage(message: MessageEntity)

    @Delete
    suspend fun deleteMessage(message: MessageEntity)

    @Query("DELETE FROM messages_table Where id == :messageId")
    suspend fun deleteMessageById(messageId: Int)
    @Query("SELECT * FROM messages_table WHERE tag LIKE :query OR message LIKE :query")
    fun search(query: String):Flow<List<MessageEntity>>
}