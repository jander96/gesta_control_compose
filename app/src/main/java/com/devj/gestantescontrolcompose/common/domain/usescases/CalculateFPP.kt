package com.devj.gestantescontrolcompose.common.domain.usescases

import com.devj.gestantescontrolcompose.common.domain.Constants.GESTATION_PERIOD_COMPLETED
import com.devj.gestantescontrolcompose.common.domain.Constants.GESTATION_PERIOD_EXTENDED
import com.devj.gestantescontrolcompose.common.domain.DateCalculator
import com.devj.gestantescontrolcompose.common.domain.model.USData
import java.util.Calendar
import javax.inject.Inject

class CalculateFPP @Inject constructor(private val dateCalculator: DateCalculator) {

    operator fun invoke( fum: String?, usData: USData): String {
        val calendar = Calendar.getInstance()
        val weeks =
            if (usData.uSWeeks != null && usData.uSDays != null && usData.uSDays == 0)
                GESTATION_PERIOD_COMPLETED - usData.uSWeeks
            else if (usData.uSWeeks != null && usData.uSDays != null && usData.uSDays > 0)
                GESTATION_PERIOD_EXTENDED - usData.uSWeeks
            else null

        return if (fum != null) {
            dateCalculator.addWeeksToADate(fum, GESTATION_PERIOD_COMPLETED)
        } else if (usData.uSDate != null && usData.uSWeeks != null && usData.uSDays != null) {
            dateCalculator.addWeeksToADate(usData.uSDate, weeks!!)
        } else {
            "Sin datos"
        }


    }
}