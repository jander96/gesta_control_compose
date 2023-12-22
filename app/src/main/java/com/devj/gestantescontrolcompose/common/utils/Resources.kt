package com.devj.gestantescontrolcompose.common.utils

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale


object DateTimeHelper{
    const val YEAR_MONTH_DAY = "yyyy-MM-dd"
    const val YEAR_MONTH_DAY_HOUR_MINUTES = "yyyy-MM-dd HH:mm"
    const val HOUR_MINUTES_am = "hh:mm a"
    const val DAY_OF_WEEK_MONTH = "EEEE dd 'de' MMMM"
    const val DAY_OF_MONTH_HOUR_MINUTES_am = "dd 'de' MMMM h:mm a"
    const val DAY_OF_MONTH_OF_YEAR = "dd 'de' MMMM 'de' yyyy"
    const val FULL_DATE_TIME_am = "yyyy-MM-dd hh:mm a"


    val yearMonthDayFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern(YEAR_MONTH_DAY)
    val yearMonthDayHourMinutesFormatter : DateTimeFormatter = DateTimeFormatter.ofPattern(YEAR_MONTH_DAY_HOUR_MINUTES)
    val hourMinutesAmFormatter : DateTimeFormatter = DateTimeFormatter.ofPattern(HOUR_MINUTES_am)
    val dayOfWeekMonthFormatter : DateTimeFormatter= DateTimeFormatter.ofPattern(DAY_OF_WEEK_MONTH)
    val dayOfMothHourMinutesFormatter : DateTimeFormatter = DateTimeFormatter.ofPattern(DAY_OF_MONTH_HOUR_MINUTES_am)
    val dayOfMonthYearFormatter : DateTimeFormatter = DateTimeFormatter.ofPattern(DAY_OF_MONTH_OF_YEAR, Locale("es", "ES"))
    val fullDateTimeAmFormatter : DateTimeFormatter= DateTimeFormatter.ofPattern(FULL_DATE_TIME_am)

    fun ymdToHumanReadableDate(inputDate: String):String{
        val inputFormat = fullDateTimeAmFormatter
        val date = inputFormat.parse(inputDate)
        val outputFormat = dayOfMothHourMinutesFormatter
        return  outputFormat.format(date)
    }


    fun LocalDateTime.toStandardDate(): String {
        val formatter = yearMonthDayFormatter
        return formatter.format(this)
    }

    fun LocalDateTime.toStandardTime(): String {
        val formatter = hourMinutesAmFormatter
        return formatter.format(this)
    }

    fun LocalDateTime.textualDate(): String {
        val formatter = dayOfWeekMonthFormatter
        return formatter.format(this)
    }

}

