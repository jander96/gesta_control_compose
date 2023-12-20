package com.devj.gestantescontrolcompose.features.scheduler.domain


data class Message(
    val id: Int = 0,
    val message: String,
    val dateTime: String,
    val tag: String,
    val addressees: List<String>
    )

