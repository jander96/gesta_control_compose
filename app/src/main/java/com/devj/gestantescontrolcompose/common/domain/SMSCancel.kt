package com.devj.gestantescontrolcompose.common.domain

import com.devj.gestantescontrolcompose.features.scheduler.domain.Message

interface SMSCancel {
    fun cancel(message: Message)
}