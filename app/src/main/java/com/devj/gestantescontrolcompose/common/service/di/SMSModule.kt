package com.devj.gestantescontrolcompose.common.service.di

import com.devj.gestantescontrolcompose.common.domain.SMSCancel
import com.devj.gestantescontrolcompose.common.domain.SMSScheduler
import com.devj.gestantescontrolcompose.common.service.work_manager.CancelSMSImpl
import com.devj.gestantescontrolcompose.common.service.work_manager.SMSSchedulerImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class SMSModule {

    @Binds
    abstract fun bindSMSScheduler(impl: SMSSchedulerImpl):SMSScheduler
    @Binds
    abstract fun bindSMSCancel(impl: CancelSMSImpl):SMSCancel
}