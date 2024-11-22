package com.devj.gestantescontrolcompose.common.data.cache.model.mapper

interface CacheMapper<in E, out D> {
    fun mapToDomain(cacheEntity: E): D
}