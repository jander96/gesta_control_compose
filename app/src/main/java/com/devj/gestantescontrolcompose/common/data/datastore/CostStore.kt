package com.devj.gestantescontrolcompose.common.data.datastore

import kotlinx.coroutines.flow.Flow

interface CostStore {
    fun getTotalCost(): Flow<Float>
    fun getCostByMessage(): Flow<Float>
    suspend fun updateCost(cost: Float)
    suspend fun updateSMSCost(cost: Float)
}