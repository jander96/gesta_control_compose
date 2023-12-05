package com.devj.gestantescontrolcompose.features.home.domain.use_case

import com.devj.gestantescontrolcompose.common.domain.PregnantRepository
import com.devj.gestantescontrolcompose.features.home.domain.HomeEffect
import javax.inject.Inject

class SearchByName @Inject constructor(private val repo: PregnantRepository) {

    suspend operator fun invoke(query: String): HomeEffect {
        return try {
            val result = repo.searchByName("%${query}%")
            HomeEffect.PregnantListUpdate.SearchResult(result)

        } catch (e: Exception) {
            HomeEffect.PregnantListUpdate.Error(e)
        }

    }
}