package com.devj.gestantescontrolcompose.common.data.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CostStoreImp @Inject constructor(@ApplicationContext context: Context)  : CostStore {
    private val dataStore = context.dataStore
    private object Key {
        val totalCost = floatPreferencesKey("total_cost")
        val costByMessage = floatPreferencesKey("cost_by_message")
    }
    override fun getTotalCost(): Flow<Float> {
        return dataStore.data.map {preference->
            preference[Key.totalCost] ?: 0f
        }
    }

    override fun getCostByMessage(): Flow<Float> {
        return dataStore.data.map {preference->
            preference[Key.costByMessage] ?: 1f
        }
    }

    override suspend fun updateCost(cost: Float) {
        dataStore.edit {mutablePreferences ->
            mutablePreferences[Key.totalCost] = cost
        }
    }

    override suspend fun updateSMSCost(cost: Float) {
        dataStore.edit {mutablePreferences ->
            mutablePreferences[Key.costByMessage] = cost
        }
    }
}