package com.devj.gestantescontrolcompose.domain.usescases

import android.util.Log
import com.devj.gestantescontrolcompose.domain.PregnantRepository
import com.devj.gestantescontrolcompose.domain.results.DetailEffect
import javax.inject.Inject

class DeletePregnantById @Inject constructor(private val repo: PregnantRepository) {
    suspend operator fun invoke(pregnantId : Int): DetailEffect {
       return try {
           repo.deletePregnantById(pregnantId)
           Log.d("FlowIntent","Se lanzó el caso de Uso")

           DetailEffect.PregnantDelete.Success
        }catch (e: Exception){
            DetailEffect.PregnantDelete.Error(e)
        }
    }
}