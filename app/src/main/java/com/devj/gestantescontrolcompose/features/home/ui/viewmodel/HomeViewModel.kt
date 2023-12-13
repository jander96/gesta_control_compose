package com.devj.gestantescontrolcompose.features.home.ui.viewmodel

import com.devj.gestantescontrolcompose.common.basemvi.MviBaseViewModel
import com.devj.gestantescontrolcompose.common.domain.usescases.DeletePregnantById
import com.devj.gestantescontrolcompose.common.domain.usescases.GetAllPregnant
import com.devj.gestantescontrolcompose.common.ui.model.UIMapper
import com.devj.gestantescontrolcompose.features.home.domain.HomeAction
import com.devj.gestantescontrolcompose.features.home.domain.HomeEffect
import com.devj.gestantescontrolcompose.features.home.domain.HomeIntent
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
            HomeAction.LoadListPregnant -> getAllPregnant()
            is HomeAction.DeletePregnant -> deletePregnant(action.id)
            is HomeAction.Search -> search(action.query)
            is HomeAction.Filter -> filterList(action.filter)
        }
    }

     override fun reduce(oldState: HomeViewState, effect: HomeEffect): HomeViewState {
        return when (effect) {
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
                    error = effect.throwable,
                    isLoading = false,
                    pregnantList = flowOf(emptyList())
                )
            }

            is HomeEffect.PregnantListUpdate.Success -> {
                oldState.copy(
                    error = null,
                    isLoading = false,
                    pregnantList = effect.listOfPregnant.map { pregnantList ->
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
                    pregnantList = effect.listOfPregnant.map { pregnantList ->
                        pregnantList.map {
                            uiMapper.fromDomain(it)
                        }
                    },
                    isDataBaseEmpty = false
                )
            }

            is HomeEffect.PregnantListUpdate.ApplyFilter -> {
                oldState.copy(
                    error = null,
                    isLoading = false,
                    pregnantList = effect.listOfPregnant.map { pregnantList ->
                        pregnantList.map {
                            uiMapper.fromDomain(it)
                        }
                    },
                    isDataBaseEmpty = false,
                    activeFilter = effect.filter
                )
            }
        }
    }
}