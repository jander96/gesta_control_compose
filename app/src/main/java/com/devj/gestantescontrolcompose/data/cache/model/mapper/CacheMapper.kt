package com.devj.gestantescontrolcompose.data.cache.model.mapper

interface CacheMapper<in E, out D> {
    fun mapToDomain(cacheEntity: E): D
}