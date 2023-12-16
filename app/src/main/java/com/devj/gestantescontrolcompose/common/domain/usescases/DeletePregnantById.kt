package com.devj.gestantescontrolcompose.common.domain.usescases

import com.devj.gestantescontrolcompose.common.domain.PregnantRepository
import javax.inject.Inject

class DeletePregnantById @Inject constructor(private val repo: PregnantRepository) {
    suspend operator fun invoke(pregnantId: Int): Result<Unit> = runCatching{
        repo.deletePregnantById(pregnantId)
    }
}