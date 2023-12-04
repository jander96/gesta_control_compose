package com.devj.gestantescontrolcompose.common.data.cache.model.mapper

import com.devj.gestantescontrolcompose.common.data.cache.model.PregnantEntity
import com.devj.gestantescontrolcompose.common.domain.model.Measures
import com.devj.gestantescontrolcompose.common.domain.model.Pregnant
import javax.inject.Inject

class PregnantEntityMapper @Inject constructor(
    private val dataDateEmbeddedMapper: DataDateEmbeddedMapper,
    private val riskFactorEmbeddedMapper: RiskFactorEmbeddedMapper
    ): CacheMapper<PregnantEntity, Pregnant> {
    override fun mapToDomain(cacheEntity: PregnantEntity): Pregnant {
        return Pregnant(
            id = cacheEntity.id,
            name = cacheEntity.name,
            lastName = cacheEntity.lastName,
            age = cacheEntity.age,
            phoneNumber = cacheEntity.phoneNumber,
            measures =  Measures(cacheEntity.measures?.weight,cacheEntity.measures?.size),
            dataDate = dataDateEmbeddedMapper.mapToDomain(cacheEntity.dataDate),
            riskFactors = cacheEntity.riskFactors?.map { riskFactorEmbeddedMapper.mapToDomain(it) },
            notes = cacheEntity.notes,
            photo = cacheEntity.photo
        )
    }
}