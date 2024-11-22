package com.devj.gestantescontrolcompose.common.data.di

import com.devj.gestantescontrolcompose.common.data.datastore.CostStore
import com.devj.gestantescontrolcompose.common.data.datastore.CostStoreImp
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DataStoreModule {
    @Binds
    abstract fun bindCostService(impl: CostStoreImp): CostStore
}