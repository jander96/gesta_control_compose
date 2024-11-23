package com.devj.gestantescontrolcompose.features.scheduler.domain
import java.time.ZonedDateTime


data class Message(
    val id: String,
    val message: String,
    val dateTime: ZonedDateTime?,
    val tag: String,
    val addressees: List<String>
    ){
    val isBeforeNow: Boolean
        get() {
            val now = ZonedDateTime.now()
            return dateTime?.isBefore(now) == true
        }
}

