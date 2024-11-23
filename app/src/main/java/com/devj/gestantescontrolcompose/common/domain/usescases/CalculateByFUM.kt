package com.devj.gestantescontrolcompose.common.domain.usescases

import com.devj.gestantescontrolcompose.common.domain.Constants.DAYS_BY_WEEKS
import com.devj.gestantescontrolcompose.common.domain.DateCalculator
import java.time.ZonedDateTime
import javax.inject.Inject

class CalculateByFUM @Inject constructor(private val dateCalculator: DateCalculator){

  operator fun invoke (date: ZonedDateTime): String? {
        val daysDiff = dateCalculator.getDaysDiff(date)
        val gestationalAge =
            "${daysDiff / DAYS_BY_WEEKS}." + "${daysDiff % DAYS_BY_WEEKS}"
        return if (gestationalAge.toFloat() < 42f) gestationalAge else null
    }
}