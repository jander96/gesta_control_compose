package com.devj.gestantescontrolcompose.features.scheduler.domain


data class Message(
    val id: Int,
    val phoneNumber: String,
    val message: String,
    val dateTime: String,
    val tag: String,
    )

