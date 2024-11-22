package com.devj.gestantescontrolcompose.common.domain.model

enum class RiskClassification(
    val level: Int,
    ) {
    HEIGHT_RISK(1),
    LOW_RISK(0);

    companion object{
        fun fromLevel(level: Int): RiskClassification{
           return when(level){
               0 -> LOW_RISK
               else -> HEIGHT_RISK
           }
        }
    }
}