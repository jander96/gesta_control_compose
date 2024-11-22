package com.devj.gestantescontrolcompose.common.presenter.model

import com.devj.gestantescontrolcompose.common.domain.model.Pregnant
import com.devj.gestantescontrolcompose.common.domain.model.RiskFactor
import com.devj.gestantescontrolcompose.common.domain.model.USData
import com.devj.gestantescontrolcompose.common.domain.usescases.CalculateByFUM
import com.devj.gestantescontrolcompose.common.domain.usescases.CalculateByUS
import com.devj.gestantescontrolcompose.common.domain.usescases.CalculateFPP
import com.devj.gestantescontrolcompose.common.extensions.ifNullReturnEmpty
import java.text.DecimalFormat
import javax.inject.Inject

class UIMapper @Inject constructor(
    private val calculateByFUM: CalculateByFUM,
    private val calculateByUS: CalculateByUS,
    private val calculateFPP: CalculateFPP
) {

    fun fromDomain(pregnant: Pregnant): PregnantUI {
        return with(pregnant) {

            PregnantUI(
                id =id,
                name =name,
                lastName = lastName,
                age = age.toString().ifNullReturnEmpty(),
                phoneNumber = phoneNumber ?: "",
                size = measures?.size.toString().ifNullReturnEmpty(),
                weight = measures?.weight.toString().ifNullReturnEmpty(),
                if (measures?.size != null && measures.weight != null)
                    getIMC(measures.weight, measures.size)
                else "",
                isFUMReliable = dataDate.isFUMReliable,
                fum = dataDate.fUM ?: "",
                gestationalAgeByFUM = if (!dataDate.fUM.isNullOrEmpty())
                    calculateByFUM(dataDate.fUM) ?: "+42"
                else "",
                dataDate.firstFUG ?: "",
                gestationalAgeByFirstUS = if (dataDate.firstFUG != null &&
                    dataDate.firstUSWeeks != null &&
                    dataDate.firstUSDays != null
                )
                    calculateByUS(
                        dataDate.firstFUG,
                        dataDate.firstUSWeeks,
                        dataDate.firstUSDays,
                    )  ?: "+42"
                else "",
                secondUS = dataDate.secondFUG ?: "",
               gestationalAgeBySecondUS =  if (dataDate.secondFUG != null &&
                    dataDate.secondUSWeeks != null &&
                    dataDate.secondUSDays != null
                )
                    calculateByUS(
                        dataDate.secondFUG,
                        dataDate.secondUSWeeks,
                        dataDate.secondUSDays,
                    ) ?: "+42"
                else "",
                thirdUS = dataDate.secondFUG ?: "",
                gestationalAgeByThirdUS = if (dataDate.thirdFUG != null &&
                    dataDate.thirdUSWeeks != null &&
                    dataDate.thirdUSDays != null
                )
                    calculateByUS(
                        dataDate.thirdFUG,
                        dataDate.thirdUSWeeks,
                        dataDate.thirdUSDays,
                    )  ?: "+42"
                else "",
               fpp =  calculateFPP(
                    dataDate.fUM,
                    USData(dataDate.firstFUG, dataDate.firstUSWeeks, dataDate.firstUSDays)
                ),
                riskClassification =     pregnant.riskClassification,
                listOfRiskFactors = if (pregnant.riskFactors != null)
                    getStringFromList(pregnant.riskFactors)
                else "",
                notes = pregnant.notes ?: "",
                photo =pregnant.photo ?: "",
                firstUSWeeks = pregnant.dataDate.firstUSWeeks.ifNullReturnEmpty(),
                firstUSDays = pregnant.dataDate.firstUSDays.ifNullReturnEmpty(),

            )
        }
    }

    private fun getIMC(weight: Double, size: Double): String {
        val formatter = DecimalFormat("#.0")
        val sizeInMeters = size / 100
        val imc = weight / (sizeInMeters * sizeInMeters)
        return formatter.format(imc).toString()
    }



    private fun getStringFromList(listOfRiskFactors: List<RiskFactor>): String {
        return listOfRiskFactors.joinToString("/ ") { it.name }
    }
}