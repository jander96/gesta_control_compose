package com.devj.gestantescontrolcompose.domain.results

import com.devj.gestantescontrolcompose.domain.model.Pregnant




import kotlinx.coroutines.flow.Flow


sealed class HomeEffect {
    sealed class PregnantListUpdate : HomeEffect() {
        object Loading : HomeEffect()
        object EmptyResponse : HomeEffect()
        data class Error(val throwable: Throwable?) : HomeEffect()
        data class Success(val listOfPregnant: Flow<List<Pregnant>>) : HomeEffect()

    }
}
