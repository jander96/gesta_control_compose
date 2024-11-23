package com.devj.gestantescontrolcompose.common.domain.usescases

import com.devj.gestantescontrolcompose.common.domain.Constants
import com.devj.gestantescontrolcompose.common.domain.DateCalculator
import java.time.ZonedDateTime
import javax.inject.Inject

class CalculateByUS @Inject constructor(private val dateCalculator: DateCalculator) {

    operator fun invoke(date: ZonedDateTime ,weeks: Int, days: Int): String? {
        val diffOfDaysFromUS = dateCalculator.getDaysDiff(date)
        val totalDiffOfDays = days + (diffOfDaysFromUS % Constants.DAYS_BY_WEEKS)
        val gestationalWeeks =
            if (totalDiffOfDays < Constants.DAYS_BY_WEEKS) {
                weeks + (diffOfDaysFromUS / Constants.DAYS_BY_WEEKS)
            } else {
                (weeks+ (diffOfDaysFromUS / Constants.DAYS_BY_WEEKS)) + 1
            }
        val gestationalDays =
            if (totalDiffOfDays < Constants.DAYS_BY_WEEKS) totalDiffOfDays
            else totalDiffOfDays - Constants.DAYS_BY_WEEKS

        val gestationalAge = "${gestationalWeeks}." + "$gestationalDays"
        return if (gestationalAge.toFloat() < 42f) gestationalAge else null
    }
}