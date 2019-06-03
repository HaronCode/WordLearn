package ru.coderedwolf.wordlearn.domain.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ru.coderedwolf.wordlearn.domain.data.converter.DateConverter
import ru.coderedwolf.wordlearn.domain.data.dao.WordsCategoryDao
import ru.coderedwolf.wordlearn.model.entity.CategoryEntity

/**
 * @author CodeRedWolf. Date 04.05.2019.
 */
@Database(entities = [CategoryEntity::class], version = 1)
@TypeConverters(value = [
    DateConverter::class
])
abstract class DataBase : RoomDatabase() {

    abstract fun wordsCategoryDao(): WordsCategoryDao
}