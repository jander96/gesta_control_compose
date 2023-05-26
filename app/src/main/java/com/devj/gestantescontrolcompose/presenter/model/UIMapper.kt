package com.devj.gestantescontrolcompose.presenter.model

import com.devj.gestantescontrolcompose.domain.model.Pregnant
import com.devj.gestantescontrolcompose.domain.model.RiskFactor
import com.devj.gestantescontrolcompose.domain.model.USData
import com.devj.gestantescontrolcompose.domain.usescases.CalculateByFUM
import com.devj.gestantescontrolcompose.domain.usescases.CalculateByUS
import com.devj.gestantescontrolcompose.domain.usescases.CalculateFPP
import com.devj.gestantescontrol.utils.ifNullReturnEmpty
import javax.inject.Inject

class UIMapper @Inject constructor(
    private val calculateByFUM: CalculateByFUM,
    private val calculateByUS: CalculateByUS,
    private val calculateFPP: CalculateFPP
) {

    fun fromDomain(pregnant: Pregnant): PregnantUI {
        return with(pregnant) {

            PregnantUI(
                id,
                name,
                lastName,
                age.toString().ifNullReturnEmpty(),
                phoneNumber ?: "",
                measures?.size.toString().ifNullReturnEmpty(),
                measures?.weight.toString().ifNullReturnEmpty(),
                if (measures?.size != null && measures.weight != null)
                    getIMC(measures.weight, measures.size).toString()
                else "Sin datos",
                dataDate.isFUMReliable,
                dataDate.fUM ?: "",
                if (dataDate.fUM != null)
                    calculateByFUM(dataDate.fUM)
                else "Sin datos",
                dataDate.firstFUG ?: "",
                if (dataDate.firstFUG != null &&
                    dataDate.firstUSWeeks != null &&
                    dataDate.firstUSDays != null
                )
                    calculateByUS(
                        dataDate.firstFUG,
                        dataDate.firstUSWeeks,
                        dataDate.firstUSDays,
                    )
                else "Sin Datos",
                dataDate.secondFUG ?: "",
                if (dataDate.secondFUG != null &&
                    dataDate.secondUSWeeks != null &&
                    dataDate.secondUSDays != null
                )
                    calculateByUS(
                        dataDate.secondFUG,
                        dataDate.secondUSWeeks,
                        dataDate.secondUSDays,
                    )
                else "Sin Datos",
                dataDate.secondFUG ?: "",
                if (dataDate.thirdFUG != null &&
                    dataDate.thirdUSWeeks != null &&
                    dataDate.thirdUSDays != null
                )
                    calculateByUS(
                        dataDate.thirdFUG,
                        dataDate.thirdUSWeeks,
                        dataDate.thirdUSDays,
                    )
                else "Sin Datos",
                calculateFPP(
                    dataDate.fUM,
                    USData(dataDate.firstFUG, dataDate.firstUSWeeks, dataDate.firstUSDays)
                ),
                        getRiskClassification (pregnant.riskFactors),
                if (pregnant.riskFactors != null)
                    getStringFromList(pregnant.riskFactors)
                else "No tiene riesgos asociados",
                pregnant.notes ?: "",
                pregnant.photo ?: ""
            )
        }
    }

    private fun getIMC(weight: Double, size: Double) = weight / (size * size)
    private fun getRiskClassification(listOfRisk: List<RiskFactor>?): String {
        return if (listOfRisk != null && listOfRisk.size > 2) "Alto riesgo" else "Bajo riesgo"
    }

    private fun getStringFromList(listOfRiskFactors: List<RiskFactor>): String {
        return listOfRiskFactors.joinToString("/ ") { it.name }
    }
}