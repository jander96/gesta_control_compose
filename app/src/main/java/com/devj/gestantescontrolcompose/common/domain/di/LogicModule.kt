package com.devj.gestantescontrolcompose.common.domain.di

import com.devj.gestantescontrolcompose.common.domain.DateCalculator
import com.devj.gestantescontrolcompose.common.service.AndroidDateCalculator
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
abstract class LogicModule {
    @Binds
    abstract fun bindDateCalculator(impl: AndroidDateCalculator): DateCalculator
}