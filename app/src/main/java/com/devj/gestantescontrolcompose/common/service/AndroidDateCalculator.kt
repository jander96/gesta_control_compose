package com.devj.gestantescontrolcompose.common.service

import com.devj.gestantescontrolcompose.common.domain.DateCalculator
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject

class AndroidDateCalculator @Inject constructor() : DateCalculator {
    private val dateFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    private val calendar = Calendar.getInstance()

    companion object {
        private const val CANT_MILLIS_X_DIA = 86400000
        private const val EXCEPTION_MESSAGE = "Invalid string format for date in yyyy/MM/dd"
    }

    override fun getDaysDiff(dateInStringType: String): Int {

        try {
            val currentDaysInMillis = calendar.timeInMillis
            val lastDateInMillis = dateFormatter.parse(dateInStringType)?.time
            return ((currentDaysInMillis - lastDateInMillis!!) / CANT_MILLIS_X_DIA).toInt()
        } catch (e: Exception) {
            throw Exception("$EXCEPTION_MESSAGE value: $dateInStringType")
        }
    }

    override fun addWeeksToADate(dateInStringType: String, weeks: Int): String {
        try {
            calendar.time = dateFormatter.parse(dateInStringType)!!
            calendar.add(Calendar.WEEK_OF_YEAR, weeks)

            val formater = DateTimeFormatter.ISO_DATE
            val localDate = Instant.ofEpochMilli(calendar.timeInMillis).atZone(ZoneId.of("UTC")).toLocalDate()
           return localDate.format(formater)

        } catch(e: Exception) {
            throw Exception("$EXCEPTION_MESSAGE value: $dateInStringType")
        }
    }
}