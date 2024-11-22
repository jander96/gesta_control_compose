package com.devj.gestantescontrolcompose.common.domain.usescases


import com.devj.gestantescontrolcompose.common.domain.PregnantRepository
import com.devj.gestantescontrolcompose.common.domain.model.Pregnant
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class GetAllPregnant @Inject constructor(private val repo: PregnantRepository) {

    operator fun invoke(): Result<Flow<List<Pregnant>>> = runCatching{
        repo.getAllPregnant()
    }
}