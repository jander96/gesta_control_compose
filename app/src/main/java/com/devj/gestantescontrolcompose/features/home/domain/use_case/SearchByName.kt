package com.devj.gestantescontrolcompose.features.home.domain.use_case

import com.devj.gestantescontrolcompose.common.domain.PregnantRepository
import com.devj.gestantescontrolcompose.common.domain.model.Pregnant
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchByName @Inject constructor(private val repo: PregnantRepository) {
    suspend operator fun invoke(query: String): Result<Flow<List<Pregnant>>> =
        runCatching {
            repo.searchByName("%${query}%")
        }
}