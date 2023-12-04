package com.devj.gestantescontrolcompose.common.domain


import com.devj.gestantescontrolcompose.common.domain.model.Pregnant
import kotlinx.coroutines.flow.Flow


interface PregnantRepository {
    fun getAllPregnant() : Flow<List<Pregnant>>
    suspend fun getPregnantById(pregnantId: Int): Pregnant

    suspend fun insertPregnant(pregnant: Pregnant)

    suspend fun deletePregnant(pregnant: Pregnant)

    suspend fun deletePregnantById(pregnantId: Int)
}