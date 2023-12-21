package com.devj.gestantescontrolcompose.features.scheduler.domain

import com.devj.gestantescontrolcompose.common.utils.DateTimeHelper
import java.time.LocalDateTime


data class Message(
    val id: Int = 0,
    val message: String,
    val dateTime: String,
    val tag: String,
    val addressees: List<String>
    ){
    val isBeforeNow: Boolean
        get() {
            val now = LocalDateTime.now()
            val messageDateTime = LocalDateTime.parse(dateTime,DateTimeHelper.fullDateTimeAmFormatter)
            return messageDateTime.isBefore(now)
        }
}

