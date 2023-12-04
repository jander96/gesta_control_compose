package com.devj.gestantescontrolcompose.common.ui.model


import android.os.Parcelable
import kotlinx.parcelize.Parcelize
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
    val fum : String,
    val gestationalAgeByFUM: String,
    val firstUS : String,
    val gestationalAgeByFirstUS: String,
    val secondUS : String,
    val gestationalAgeBySecondUS: String,
    val thirdUS : String,
    val gestationalAgeByThirdUS: String,
    val fpp : String,
    val riskClassification: String,
    val listOfRiskFactors: String,
    val notes: String,
    val photo: String,
    val firstUSWeeks: String,
    val firstUSDays: String,
) : Parcelable







