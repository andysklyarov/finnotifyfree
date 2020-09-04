package com.andysklyarov.finnotify.framework.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CurrencyOnDate(
    @PrimaryKey
    @ColumnInfo(name = "id") val id: Long,

    @ColumnInfo(name = "Vname") val valName: String,
    @ColumnInfo(name = "Vnom")  val nom: String,
    @ColumnInfo(name = "Vcurs") val curs: String,
    @ColumnInfo(name = "Vcode") val code: String,
    @ColumnInfo(name = "VchCode") val chCode: String,
    @ColumnInfo(name = "DateTime") val date: String
)