package com.devj.gestantescontrolcompose.features.scheduler.domain.use_case

import com.devj.gestantescontrolcompose.common.data.datastore.CostStore
import javax.inject.Inject

class UpdateCost @Inject constructor(private val costStore: CostStore) {
    suspend operator fun invoke (cost: Float) = runCatching {
        costStore.updateCost(cost)
    }
}