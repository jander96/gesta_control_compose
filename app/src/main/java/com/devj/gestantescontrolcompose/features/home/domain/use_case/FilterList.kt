package com.devj.gestantescontrolcompose.features.home.domain.use_case

import com.devj.gestantescontrolcompose.common.domain.PregnantRepository
import com.devj.gestantescontrolcompose.common.domain.model.Pregnant
import com.devj.gestantescontrolcompose.common.domain.model.RiskClassification
import com.devj.gestantescontrolcompose.features.home.domain.model.FilterType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

typealias FilteredPregnant = Pair<Flow<List<Pregnant>>, FilterType?>

class FilterList @Inject constructor(private val repo: PregnantRepository) {

    operator fun invoke(filter: FilterType?): Result<FilteredPregnant> = runCatching {

        val result = repo.getAllPregnant()
        val list = when (filter) {
            FilterType.onRisk -> result.map { list ->
                list.filter {
                    it.riskClassification == RiskClassification.HEIGHT_RISK
                }
            }

            else -> result
        }
        (list to filter)

    }
}