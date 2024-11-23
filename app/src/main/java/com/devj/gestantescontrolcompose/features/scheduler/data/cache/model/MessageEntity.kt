package com.devj.gestantescontrolcompose.features.scheduler.data.cache.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.devj.gestantescontrolcompose.common.utils.DateTimeHelper.toIsoDate
import com.devj.gestantescontrolcompose.features.scheduler.domain.Message
import java.time.ZonedDateTime

@Entity(tableName = "messages_table")
data class MessageEntity(
    @PrimaryKey
    val id: String,
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
                dateTime = message.dateTime?.toIsoDate() ?: "",
                tag = message.tag,
                addressees = message.addressees
            )
    }

    fun toDomain(): Message =
        Message(id, message, ZonedDateTime.parse(dateTime), tag, addressees)
}