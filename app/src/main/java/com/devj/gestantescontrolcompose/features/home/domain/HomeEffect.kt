package com.devj.gestantescontrolcompose.features.home.domain




import com.devj.gestantescontrolcompose.app.navigation.Home
import com.devj.gestantescontrolcompose.common.basemvi.MviResult
import com.devj.gestantescontrolcompose.common.domain.model.Pregnant
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf


sealed class HomeEffect : MviResult {
    sealed class PregnantListUpdate : HomeEffect() {
        object Loading : HomeEffect()
        object EmptyResponse : HomeEffect()
        data class Error(val throwable: Throwable?) : HomeEffect()
        data class Success(
            val listOfPregnant: Flow<List<Pregnant>>,
        ) : HomeEffect()
        data class SearchResult(val listOfPregnant: Flow<List<Pregnant>>) : HomeEffect()

        object DeleteSuccessfully : HomeEffect()

    }
    data class StatsResult(
        val total: Flow<Int> = flowOf(0),
        val onRisk: Flow<Int> = flowOf(0),
        val onFinalPeriod: Flow<Int> = flowOf(0),
        ) : HomeEffect()

}
