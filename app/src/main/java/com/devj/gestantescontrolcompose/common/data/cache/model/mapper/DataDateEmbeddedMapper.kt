package com.devj.gestantescontrolcompose.common.data.cache.model.mapper

import com.devj.gestantescontrolcompose.common.data.cache.model.DataDateEmbedded
import com.devj.gestantescontrolcompose.common.domain.model.DataDate
import javax.inject.Inject

class DataDateEmbeddedMapper @Inject constructor(): CacheMapper<DataDateEmbedded, DataDate> {
    override fun mapToDomain(cacheEntity: DataDateEmbedded): DataDate {
        return DataDate(
            fUM = cacheEntity.fUM,
            isFUMReliable = cacheEntity.isFUMReliable,
            firstFUG = cacheEntity.firstFUG,
            firstUSWeeks = cacheEntity.firstUSWeeks,
            firstUSDays = cacheEntity.firstUSDays,
            secondFUG = cacheEntity.secondFUG,
            secondUSWeeks = cacheEntity.secondUSWeeks,
            secondUSDays = cacheEntity.secondUSDays,
            thirdFUG = cacheEntity.thirdFUG,
            thirdUSWeeks = cacheEntity.thirdUSWeeks,
            thirdUSDays = cacheEntity.thirdUSDays
        )
    }
}