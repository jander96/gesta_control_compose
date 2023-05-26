package com.devj.gestantescontrolcompose.domain.usescases


import com.devj.gestantescontrolcompose.domain.PregnantRepository
import com.devj.gestantescontrolcompose.domain.results.HomeEffect
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