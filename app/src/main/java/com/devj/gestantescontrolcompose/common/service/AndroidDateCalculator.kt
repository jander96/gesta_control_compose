package com.devj.gestantescontrolcompose.common.service

import com.devj.gestantescontrolcompose.common.domain.DateCalculator
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject

class AndroidDateCalculator @Inject constructor() : DateCalculator {
    private val dateFormatter = SimpleDateFormat("d/M/yyyy", Locale.getDefault())
    private val calendar = Calendar.getInstance()

    companion object {
        private const val CANT_MILLIS_X_DIA = 86400000
        private const val EXCEPTION_MESSAGE = "Invalid string format for date in d/M/yyyy"
    }

    override fun getDaysDiff(dateInStringType: String): Int {

        try {
            val currentDaysInMillis = calendar.timeInMillis
            val lastDateInMillis = dateFormatter.parse(dateInStringType)?.time
            return ((currentDaysInMillis - lastDateInMillis!!) / CANT_MILLIS_X_DIA).toInt()
        } catch (e: Exception) {
            throw Exception(EXCEPTION_MESSAGE)
        }
    }

    override fun addWeeksToADate(dateInStringType: String, weeks: Int): String {
        try {
            calendar.time = dateFormatter.parse(dateInStringType)!!
            calendar.add(Calendar.WEEK_OF_YEAR, weeks)

            val day = calendar.get(Calendar.DAY_OF_MONTH)
            val month = calendar.get(Calendar.MONTH) + 1
            val year = calendar.get(Calendar.YEAR)

          return  "${day}/${month}/${year}"

        } catch(e: Exception) {
            throw Exception(EXCEPTION_MESSAGE)
        }
    }
}