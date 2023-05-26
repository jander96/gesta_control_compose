package com.devj.gestantescontrolcompose.data.cache

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.devj.gestantescontrolcompose.data.cache.converters.Converter
import com.devj.gestantescontrolcompose.data.cache.dao.PregnantDao
import com.devj.gestantescontrolcompose.data.cache.model.PregnantEntity

@Database(entities = [PregnantEntity::class], version = 1, exportSchema = false)
@TypeConverters(Converter::class)
abstract class PregnantDatabase:RoomDatabase() {
    abstract fun getPregnantDao(): PregnantDao

}