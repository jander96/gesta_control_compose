package com.devj.gestantescontrolcompose.app.navigation


import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.serialization.Serializable

@Serializable
sealed interface Destination {
    companion object {
        val START_DESTINATION = Home
    }
}

@Serializable
data object Home : Destination

@Serializable
data class Edition(val pregnantId: Int? = null): Destination
@Serializable
data object Calculator: Destination
@Serializable
data object Scheduler: Destination


class AppNavigationState {
    private val _destination: MutableStateFlow<Destination> = MutableStateFlow(Home)
    val destination: MutableStateFlow<Destination> = _destination

    fun navigate(destination: Destination) {
        _destination.value = destination

    }
}