package com.jeture.budget.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "categories")
data class CategoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val name: String,
    val iconKey: String? = null,
    val colorKey: String? = null,
    val monthlyLimitCents: Long? = null
)
