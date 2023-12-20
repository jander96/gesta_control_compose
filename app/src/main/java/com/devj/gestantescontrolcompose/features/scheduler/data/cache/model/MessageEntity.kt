package com.devj.gestantescontrolcompose.features.scheduler.data.cache.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.devj.gestantescontrolcompose.features.scheduler.domain.Message

@Entity(tableName = "messages_table")
data class MessageEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val message: String,
    @ColumnInfo(name = "date_time")
    val dateTime: String,
    val tag: String,
    val addressees: List<String>
) {
    companion object {
        fun fromDomain(message: Message) =
            MessageEntity(
                id = message.id,
                message = message.message,
                dateTime = message.dateTime,
                tag = message.tag,
                addressees = message.addressees
            )
    }

    fun toDomain(): Message =
        Message(id, message, dateTime, tag,addressees)
}