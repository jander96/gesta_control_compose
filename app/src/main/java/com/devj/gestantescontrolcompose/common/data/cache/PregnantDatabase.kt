package com.devj.gestantescontrolcompose.common.data.cache

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.devj.gestantescontrolcompose.common.data.cache.converters.Converter
import com.devj.gestantescontrolcompose.common.data.cache.dao.PregnantDao
import com.devj.gestantescontrolcompose.common.data.cache.model.PregnantEntity
import com.devj.gestantescontrolcompose.features.scheduler.data.cache.dao.MessageDao
import com.devj.gestantescontrolcompose.features.scheduler.data.cache.model.MessageEntity

@Database(
    entities = [PregnantEntity::class, MessageEntity::class],
    version = 1,
    exportSchema = false,
)
@TypeConverters(Converter::class)
abstract class PregnantDatabase:RoomDatabase() {
    abstract fun getPregnantDao(): PregnantDao

    abstract fun getMessageDao(): MessageDao

}