package com.devj.gestantescontrolcompose.features.scheduler.domain.use_case

import com.devj.gestantescontrolcompose.common.data.datastore.CostStore
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCost @Inject constructor(private val costStore: CostStore) {

    operator fun invoke(): Result<Flow<Float>> = runCatching {
        costStore.getTotalCost()
    }
}