package com.jeture.budget.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.jeture.budget.data.dao.CategoryDao
import com.jeture.budget.data.dao.TransactionDao
import com.jeture.budget.data.entity.CategoryEntity
import com.jeture.budget.data.entity.TransactionEntity

@Database(
    entities = [CategoryEntity::class, TransactionEntity::class],
    version = 1,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun categoryDao(): CategoryDao
    abstract fun transactionDao(): TransactionDao
}
