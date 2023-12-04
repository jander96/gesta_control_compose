package com.devj.gestantescontrolcompose.common.data.cache.model.mapper

import com.devj.gestantescontrolcompose.common.data.cache.model.RiskFactorEmbedded
import com.devj.gestantescontrolcompose.common.domain.model.RiskFactor
import javax.inject.Inject

class RiskFactorEmbeddedMapper @Inject constructor(): CacheMapper<RiskFactorEmbedded, RiskFactor> {
    override fun mapToDomain(cacheEntity: RiskFactorEmbedded)=  RiskFactor(cacheEntity.name)
}