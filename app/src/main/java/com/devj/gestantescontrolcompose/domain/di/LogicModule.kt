package com.devj.gestantescontrolcompose.domain.di

import com.devj.gestantescontrolcompose.domain.DateCalculator
import com.devj.gestantescontrolcompose.framework.AndroidDateCalculator
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