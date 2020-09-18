package com.andysklyarov.data.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CurrencyOnDate(
    @PrimaryKey
    @ColumnInfo(name = "id") val id: Long,

    @ColumnInfo(name = "FullName") val fullName: String,
    @ColumnInfo(name = "ChCode") val chCode: String,
    @ColumnInfo(name = "DateTime") val date: String,
    @ColumnInfo(name = "Nom") val nom: Int,
    @ColumnInfo(name = "Value") val value: Float,
    @ColumnInfo(name = "Diff") val diff: Float
)