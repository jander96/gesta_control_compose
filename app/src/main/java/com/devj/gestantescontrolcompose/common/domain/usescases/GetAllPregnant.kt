package com.devj.gestantescontrolcompose.common.domain.usescases


import com.devj.gestantescontrolcompose.common.domain.PregnantRepository
import com.devj.gestantescontrolcompose.features.home.domain.HomeEffect
import javax.inject.Inject


class GetAllPregnant @Inject constructor(private val repo: PregnantRepository) {

    operator fun invoke(): HomeEffect {
        return try {
            val result = repo.getAllPregnant()
            HomeEffect.PregnantListUpdate.Success(result)

        } catch (e: Exception) {
            HomeEffect.PregnantListUpdate.Error(e)
        }

    }
}