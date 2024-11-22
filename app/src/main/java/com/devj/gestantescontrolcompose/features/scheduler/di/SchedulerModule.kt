package com.devj.gestantescontrolcompose.features.scheduler.di

import com.devj.gestantescontrolcompose.common.data.cache.PregnantDatabase
import com.devj.gestantescontrolcompose.features.scheduler.data.MessageRepositoryImpl
import com.devj.gestantescontrolcompose.features.scheduler.data.cache.dao.MessageDao
import com.devj.gestantescontrolcompose.features.scheduler.domain.MessageRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object  SchedulerModule {

    @Provides
    fun provideMessageDao(database: PregnantDatabase): MessageDao {
        return database.getMessageDao()
    }

}

@Module
@InstallIn(SingletonComponent::class)
abstract class ScheduleRespoModule(){
    @Binds
    abstract fun bindMessageRepository(impl: MessageRepositoryImpl): MessageRepository
}