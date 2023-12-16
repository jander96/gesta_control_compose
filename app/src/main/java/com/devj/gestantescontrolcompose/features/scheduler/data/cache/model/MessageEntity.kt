package com.devj.gestantescontrolcompose.features.scheduler.data.cache.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.devj.gestantescontrolcompose.features.scheduler.domain.Message

@Entity(tableName = "messages_table")
data class MessageEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "phone_number")
    val phoneNumber: String,
    val message: String,
    @ColumnInfo(name = "date_time")
    val dateTime: String,
    val tag: String,
) {
    companion object {
        fun fromDomain(message: Message) =
            MessageEntity(
                id = message.id,
                phoneNumber = message.phoneNumber,
                message = message.message,
                dateTime = message.dateTime,
                tag = message.tag
            )
    }

    fun toDomain(): Message =
        Message(id,phoneNumber, message, dateTime, tag)
}