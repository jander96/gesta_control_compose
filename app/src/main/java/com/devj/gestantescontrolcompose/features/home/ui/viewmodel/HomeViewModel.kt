package com.devj.gestantescontrolcompose.features.home.ui.viewmodel

import com.devj.gestantescontrolcompose.common.basemvi.MviBaseViewModel
import com.devj.gestantescontrolcompose.common.domain.model.RiskClassification
import com.devj.gestantescontrolcompose.common.domain.usescases.DeletePregnantById
import com.devj.gestantescontrolcompose.common.domain.usescases.GetAllPregnant
import com.devj.gestantescontrolcompose.common.presenter.model.PregnantUI
import com.devj.gestantescontrolcompose.common.presenter.model.UIMapper
import com.devj.gestantescontrolcompose.features.home.domain.HomeAction
import com.devj.gestantescontrolcompose.features.home.domain.HomeEffect
import com.devj.gestantescontrolcompose.features.home.domain.HomeIntent
import com.devj.gestantescontrolcompose.features.home.domain.use_case.SearchByName
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.map
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getAllPregnant: GetAllPregnant,
    private val deletePregnant: DeletePregnantById,
    private val uiMapper: UIMapper,
    private val search: SearchByName,
    ) : MviBaseViewModel<HomeIntent,HomeAction,HomeEffect, HomeViewState>() {


    override val mutableViewState = MutableStateFlow(HomeViewState())

    init {
        sendUiEvent(HomeIntent.EnterAtHome)
        sendUiEvent(HomeIntent.LoadStats)
    }

     override suspend fun process(action: HomeAction): HomeEffect {
        return when (action) {
            HomeAction.LoadStats -> loadStats()
            HomeAction.LoadRequirements -> load()
            is HomeAction.DeletePregnant -> delete(action.id)
            is HomeAction.Search -> searchByFullName(action.query)
        }
    }

    private suspend fun load(): HomeEffect {
        return getAllPregnant().fold(
            onSuccess = {result->
                HomeEffect.PregnantListUpdate.Success(result)
            },
            onFailure = { error -> HomeEffect.PregnantListUpdate.Error(error) }
        )
    }

    private fun loadStats(): HomeEffect {
        return getAllPregnant().fold(
            onSuccess = {result->

                HomeEffect.StatsResult(
                    onRisk = result.map { list->
                        list.map { uiMapper.fromDomain(it) }.count {
                            it.riskClassification == RiskClassification.HEIGHT_RISK
                        }
                    },
                    onFinalPeriod = result.map { list->
                        list.map { uiMapper.fromDomain(it) }.count {
                            if (it.isFUMReliable) it.gestationalAgeByFUM.toFloat() >= 37.0
                            else it.gestationalAgeByFirstUS.toFloat() >= 37.0
                        }
                    },
                    total =  result.map {list->
                        list.map { uiMapper.fromDomain(it) }.count()
                    },
                )
            },
            onFailure = {  HomeEffect.StatsResult() }
        )
    }
    private suspend fun delete(id: Int): HomeEffect{
        return deletePregnant(id).fold(
            onSuccess = {
                HomeEffect.PregnantListUpdate.DeleteSuccessfully
            },
            onFailure = { error -> HomeEffect.PregnantListUpdate.Error(error) }
        )
    }

    private suspend fun searchByFullName(query: String):HomeEffect{
        return search(query).fold(
            onSuccess = { result ->
                HomeEffect.PregnantListUpdate.SearchResult(result)
            },
            onFailure = { error -> HomeEffect.PregnantListUpdate.Error(error) }
        )
    }

     override fun reduce(oldState: HomeViewState, result: HomeEffect): HomeViewState {
        return when (result) {
            HomeEffect.PregnantListUpdate.Loading -> {
                oldState.copy(
                    error = null,
                    isLoading = true,
                    pregnantList = flowOf(emptyList())
                )
            }
            HomeEffect.PregnantListUpdate.EmptyResponse -> {
                oldState.copy(
                    isLoading = false,
                    pregnantList = flowOf(emptyList()),
                    error = null,
                    isDataBaseEmpty = true
                )
            }

            is HomeEffect.PregnantListUpdate.Error -> {
                oldState.copy(
                    error = result.throwable,
                    isLoading = false,
                    pregnantList = flowOf(emptyList())
                )
            }

            is HomeEffect.PregnantListUpdate.Success -> {
                oldState.copy(
                    error = null,
                    isLoading = false,
                    pregnantList = result.listOfPregnant.map { pregnantList ->
                        pregnantList.map {
                          uiMapper.fromDomain(it)
                        }
                    },
                    isDataBaseEmpty = false,
                )
            }

            HomeEffect.PregnantListUpdate.DeleteSuccessfully -> {
                oldState.copy(
                    error = null,
                    isLoading = false,
                )
            }

            is HomeEffect.PregnantListUpdate.SearchResult -> {
                oldState.copy(
                    error = null,
                    isLoading = false,
                    pregnantList = result.listOfPregnant.map { pregnantList ->
                        pregnantList.map { uiMapper.fromDomain(it) }
                    },
                    isDataBaseEmpty = false
                )
            }

            is HomeEffect.StatsResult -> oldState.copy(
                total = result.total,
                onRisk = result.onRisk,
                onFinalPeriod = result.onFinalPeriod
            )
        }
    }
}