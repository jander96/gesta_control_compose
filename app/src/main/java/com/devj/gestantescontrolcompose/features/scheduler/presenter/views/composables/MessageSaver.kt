package com.devj.gestantescontrolcompose.features.scheduler.presenter.views.composables
import androidx.compose.runtime.saveable.mapSaver
import com.devj.gestantescontrolcompose.common.utils.DateTimeHelper.toIsoDate
import com.devj.gestantescontrolcompose.features.scheduler.domain.Message
import java.time.ZonedDateTime

val MessageSaver = run {
    val idKey = "id"
    val messageKey = "message"
    val dateTimeKey = "dateTime"
    val tagKey = "tag"
    val addresseesKey = "addressees"

    mapSaver<Message?>(
        save = { message ->
            mapOf(
                idKey to message?.id,
                messageKey to message?.message,
                dateTimeKey to message?.dateTime?.toIsoDate(),
                tagKey to message?.tag,
                addresseesKey to message?.addressees?.joinToString(",")
            )
        },
        restore = { map ->
            if (map[idKey] == null) return@mapSaver null
            Message(
                id = map[idKey] as String,
                message = map[messageKey] as String,
                dateTime = map[dateTimeKey]?.let { ZonedDateTime.parse(it as String) },
                tag = map[tagKey] as String,
                addressees = (map[addresseesKey] as String?)?.split(",") ?: emptyList()
            )
        }
    )
}