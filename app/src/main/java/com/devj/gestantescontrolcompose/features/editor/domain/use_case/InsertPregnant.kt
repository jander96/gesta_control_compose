package com.devj.gestantescontrolcompose.features.editor.domain.use_case


import com.devj.gestantescontrolcompose.common.domain.PregnantRepository
import com.devj.gestantescontrolcompose.common.domain.model.Pregnant
import javax.inject.Inject


class InsertPregnant @Inject constructor(private val repo: PregnantRepository) {
    suspend operator fun invoke(pregnant: Pregnant): Result<Unit> = runCatching {
        repo.insertPregnant(pregnant)
    }
}