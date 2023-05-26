package com.devj.gestantescontrolcompose.data.cache.model.mapper

import com.devj.gestantescontrolcompose.data.cache.model.RiskFactorEmbedded
import com.devj.gestantescontrolcompose.domain.model.RiskFactor
import javax.inject.Inject

class RiskFactorEmbeddedMapper @Inject constructor(): CacheMapper<RiskFactorEmbedded, RiskFactor> {
    override fun mapToDomain(cacheEntity: RiskFactorEmbedded)=  RiskFactor(cacheEntity.name)
}