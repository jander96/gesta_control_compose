package com.devj.gestantescontrol.data.di

import com.devj.gestantescontrol.data.Cache
import com.devj.gestantescontrolcompose.data.PregnantRepositoryImp
import com.devj.gestantescontrol.data.cache.RoomCache
import com.devj.gestantescontrolcompose.domain.PregnantRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun bindPregnantRepository(impl: PregnantRepositoryImp): PregnantRepository

    @Binds
    abstract fun bindCache (impl : RoomCache): Cache
}