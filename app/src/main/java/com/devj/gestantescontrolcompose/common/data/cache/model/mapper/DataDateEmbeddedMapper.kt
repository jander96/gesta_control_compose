package com.devj.gestantescontrolcompose.common.data.cache.model.mapper

import com.devj.gestantescontrolcompose.common.data.cache.model.DataDateEmbedded
import com.devj.gestantescontrolcompose.common.domain.model.DataDate
import java.time.ZonedDateTime
import javax.inject.Inject

class DataDateEmbeddedMapper @Inject constructor(): CacheMapper<DataDateEmbedded, DataDate> {
    override fun mapToDomain(cacheEntity: DataDateEmbedded): DataDate {
        return DataDate(
            fUM = cacheEntity.fUM?.let { ZonedDateTime.parse(it) },
            isFUMReliable = cacheEntity.isFUMReliable,
            firstFUG = cacheEntity.firstFUG?.let { ZonedDateTime.parse(it) } ,
            firstUSWeeks = cacheEntity.firstUSWeeks,
            firstUSDays = cacheEntity.firstUSDays,
            secondFUG = cacheEntity.secondFUG?.let { ZonedDateTime.parse(it) },
            secondUSWeeks = cacheEntity.secondUSWeeks,
            secondUSDays = cacheEntity.secondUSDays,
            thirdFUG = cacheEntity.thirdFUG?.let { ZonedDateTime.parse(it) },
            thirdUSWeeks = cacheEntity.thirdUSWeeks,
            thirdUSDays = cacheEntity.thirdUSDays
        )
    }
}