package com.devj.gestantescontrolcompose.domain.usescases

import com.devj.gestantescontrolcompose.domain.PregnantRepository
import com.devj.gestantescontrolcompose.domain.model.Pregnant
import javax.inject.Inject

class DeletePregnant @Inject constructor(private val repo : PregnantRepository) {
    suspend operator fun invoke(pregnant: Pregnant){
        repo.deletePregnant(pregnant)
    }
}