package com.devj.gestantescontrolcompose.features.home.ui.viewmodel

import com.devj.gestantescontrolcompose.common.basemvi.MviBaseViewModel
import com.devj.gestantescontrolcompose.common.domain.usescases.DeletePregnantById
import com.devj.gestantescontrolcompose.common.domain.usescases.GetAllPregnant
import com.devj.gestantescontrolcompose.common.ui.model.UIMapper
import com.devj.gestantescontrolcompose.features.home.domain.HomeAction
import com.devj.gestantescontrolcompose.features.home.domain.HomeEffect
import com.devj.gestantescontrolcompose.features.home.domain.HomeIntent
import com.devj.gestantescontrolcompose.features.home.domain.model.FilterType
import com.devj.gestantescontrolcompose.features.home.domain.use_case.FilterList
import com.devj.gestantescontrolcompose.features.home.domain.use_case.SearchByName
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getAllPregnant: GetAllPregnant,
    private val deletePregnant: DeletePregnantById,
    private val uiMapper: UIMapper,
    private val search: SearchByName,
    private val filterList: FilterList,

) : MviBaseViewModel<HomeIntent,HomeAction,HomeEffect, HomeViewState>() {


    override val mutableViewState = MutableStateFlow(HomeViewState())

    init {
        sendUiEvent(HomeIntent.EnterAtHome)
    }

     override suspend fun process(action: HomeAction): HomeEffect {
        return when (action) {
            HomeAction.LoadListPregnant -> load()
            is HomeAction.DeletePregnant -> delete(action.id)
            is HomeAction.Search -> searchByFullName(action.query)
            is HomeAction.Filter -> applyFilter(action.filter)
        }
    }

    private fun load():HomeEffect{
        return getAllPregnant().fold(
            onSuccess = {result->
                HomeEffect.PregnantListUpdate.Success(result)
            },
            onFailure = { error -> HomeEffect.PregnantListUpdate.Error(error) }
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

    private fun applyFilter(filter: FilterType?): HomeEffect {
        return filterList(filter).fold(
            onSuccess = { result ->
                HomeEffect.PregnantListUpdate.ApplyFilter(
                    result.first, result.second
                )
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

            is HomeEffect.PregnantListUpdate.ApplyFilter -> {
                oldState.copy(
                    error = null,
                    isLoading = false,
                    pregnantList = result.listOfPregnant.map { pregnantList ->
                        pregnantList.map { uiMapper.fromDomain(it) }
                    },
                    isDataBaseEmpty = false,
                    activeFilter = result.filter
                )
            }
        }
    }
}