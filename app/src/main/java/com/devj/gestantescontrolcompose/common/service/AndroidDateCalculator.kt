package com.devj.gestantescontrolcompose.common.service

import com.devj.gestantescontrolcompose.common.domain.DateCalculator
import java.time.ZonedDateTime
import javax.inject.Inject

class AndroidDateCalculator @Inject constructor() : DateCalculator {

    companion object {
        private const val CANT_MILLIS_X_DIA = 86400000
    }

    override fun getDaysDiff(dateTime: ZonedDateTime): Int {
        val currentDaysInMillis = ZonedDateTime.now().toInstant().toEpochMilli()
        val lastDateInMillis = dateTime.toInstant().toEpochMilli()
        return ((currentDaysInMillis - lastDateInMillis) / CANT_MILLIS_X_DIA).toInt()
    }

    override fun addWeeksToADate(dateTime: ZonedDateTime, weeks: Int): ZonedDateTime {
        return dateTime.plusWeeks(weeks.toLong())
    }
}