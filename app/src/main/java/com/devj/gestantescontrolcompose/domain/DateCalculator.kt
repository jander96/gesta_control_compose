package com.devj.gestantescontrolcompose.domain

import java.util.*

interface DateCalculator {
    fun getDaysDiff(dateInStringType: String, calendar: Calendar): Int
    fun addWeeksToADate(dateInStringType: String, weeks: Int, calendar: Calendar): String
}