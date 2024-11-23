package com.devj.gestantescontrolcompose.common.domain

import java.time.ZonedDateTime

interface DateCalculator {
    fun getDaysDiff(dateTime: ZonedDateTime): Int
    fun addWeeksToADate(dateTime: ZonedDateTime, weeks: Int): ZonedDateTime
}