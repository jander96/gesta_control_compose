package com.devj.gestantescontrolcompose.features.home.domain.use_case

import com.devj.gestantescontrolcompose.common.domain.PregnantRepository
import com.devj.gestantescontrolcompose.common.domain.model.RiskClassification
import com.devj.gestantescontrolcompose.features.home.domain.HomeEffect
import com.devj.gestantescontrolcompose.features.home.domain.model.FilterType
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class FilterList @Inject constructor(private val repo: PregnantRepository) {

    suspend operator fun invoke(filter: FilterType?): HomeEffect {
        return try {
            val result = repo.getAllPregnant()
            val list = when(filter){
                FilterType.onRisk -> result.map {list->
                    list.filter {
                        it.riskClassification == RiskClassification.HEIGHT_RISK
                    }
                }

                else -> result
            }
            return HomeEffect.PregnantListUpdate.ApplyFilter(list, filter)

        } catch (e: Exception) {
            HomeEffect.PregnantListUpdate.Error(e)
        }

    }
}