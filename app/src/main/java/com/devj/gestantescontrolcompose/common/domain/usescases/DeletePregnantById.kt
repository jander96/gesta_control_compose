package com.devj.gestantescontrolcompose.common.domain.usescases

import com.devj.gestantescontrolcompose.common.domain.PregnantRepository
import com.devj.gestantescontrolcompose.features.home.domain.HomeEffect
import javax.inject.Inject

class DeletePregnantById @Inject constructor(private val repo: PregnantRepository) {
    suspend operator fun invoke(pregnantId : Int): HomeEffect {
       return try {
           repo.deletePregnantById(pregnantId)

           HomeEffect.PregnantListUpdate.DeleteSuccessfully
        }catch (e: Exception){
           HomeEffect.PregnantListUpdate.Error(e)
        }
    }
}