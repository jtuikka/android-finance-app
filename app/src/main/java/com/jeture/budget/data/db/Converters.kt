package com.jeture.budget.data.db

import androidx.room.TypeConverter
import com.jeture.budget.domain.model.TransactionType

class Converters {
    @TypeConverter fun fromType(value: TransactionType): String = value.name
    @TypeConverter fun toType(value: String): TransactionType = TransactionType.valueOf(value)
}
