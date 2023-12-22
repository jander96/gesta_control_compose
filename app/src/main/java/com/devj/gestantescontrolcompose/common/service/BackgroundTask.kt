package com.devj.gestantescontrolcompose.common.service

interface BackgroundTask<T> {
    fun runTask(data: T)
}