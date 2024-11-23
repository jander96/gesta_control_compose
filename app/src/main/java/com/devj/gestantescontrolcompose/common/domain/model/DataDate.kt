package com.devj.gestantescontrolcompose.common.domain.model

import java.time.ZonedDateTime

data class DataDate(
    val fUM: ZonedDateTime? = null,
    val isFUMReliable: Boolean = false,
    val firstFUG: ZonedDateTime? = null,
    val firstUSWeeks: Int? = null,
    val firstUSDays: Int? = null,
    val secondFUG: ZonedDateTime? = null,
    val secondUSWeeks: Int? = null,
    val secondUSDays: Int? = null,
    val thirdFUG: ZonedDateTime? = null,
    val thirdUSWeeks: Int? = null,
    val thirdUSDays: Int? = null,
)