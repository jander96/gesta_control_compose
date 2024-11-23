package com.devj.gestantescontrolcompose.common.presenter.model


import android.os.Parcelable
import com.devj.gestantescontrolcompose.common.domain.model.RiskClassification
import kotlinx.parcelize.Parcelize
import java.time.ZonedDateTime

@Parcelize
data class PregnantUI(
    val id: Int,
    val name: String,
    val lastName : String,
    val age: String,
    val phoneNumber: String,
    val size: String,
    val weight: String,
    val iMC: String,
    val isFUMReliable: Boolean,
    val fum : ZonedDateTime?,
    val gestationalAgeByFUM: String,
    val firstUS : ZonedDateTime?,
    val gestationalAgeByFirstUS: String,
    val secondUS : ZonedDateTime?,
    val gestationalAgeBySecondUS: String,
    val thirdUS : ZonedDateTime?,
    val gestationalAgeByThirdUS: String,
    val fpp : String,
    val riskClassification: RiskClassification,
    val listOfRiskFactors: String,
    val notes: String,
    val photo: String,
    val firstUSWeeks: String,
    val firstUSDays: String,
) : Parcelable







