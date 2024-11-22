package com.devj.gestantescontrolcompose.common.domain.usescases

import com.devj.gestantescontrolcompose.common.domain.PregnantRepository
import com.devj.gestantescontrolcompose.common.domain.model.Pregnant
import javax.inject.Inject

class DeletePregnant @Inject constructor(private val repo : PregnantRepository) {
    suspend operator fun invoke(pregnant: Pregnant): Result<Unit> = runCatching{
        repo.deletePregnant(pregnant)
    }
}