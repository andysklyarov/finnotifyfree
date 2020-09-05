package com.andysklyarov.finnotifyfree.framework.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class LastDate(
    @PrimaryKey()
    @ColumnInfo(name = "id") val id: Int,

    @ColumnInfo(name = "date") val date: String
)