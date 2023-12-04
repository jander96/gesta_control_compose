package com.devj.gestantescontrolcompose.common.domain.usescases

import com.devj.gestantescontrolcompose.common.domain.DateCalculator
import javax.inject.Inject

class CalculateByUS @Inject constructor(private val dateCalculator: DateCalculator) {

    operator fun invoke(date: String,weeks: Int, days: Int): String {
        val diffOfDaysFromUS = dateCalculator.getDaysDiff(date)
        val totalDiffOfDays = days + (diffOfDaysFromUS % com.devj.gestantescontrolcompose.common.domain.Constants.DAYS_BY_WEEKS)
        val weeks =
            if (totalDiffOfDays < com.devj.gestantescontrolcompose.common.domain.Constants.DAYS_BY_WEEKS) {
                weeks + (diffOfDaysFromUS / com.devj.gestantescontrolcompose.common.domain.Constants.DAYS_BY_WEEKS)
            } else {
                (weeks+ (diffOfDaysFromUS / com.devj.gestantescontrolcompose.common.domain.Constants.DAYS_BY_WEEKS)) + 1
            }
        val days =
            if (totalDiffOfDays < com.devj.gestantescontrolcompose.common.domain.Constants.DAYS_BY_WEEKS) totalDiffOfDays
            else totalDiffOfDays - com.devj.gestantescontrolcompose.common.domain.Constants.DAYS_BY_WEEKS

        val gestationalAge = "${weeks}." + "$days"
        return if (gestationalAge.toFloat() < 42f) gestationalAge else " Postérmino"
    }
}