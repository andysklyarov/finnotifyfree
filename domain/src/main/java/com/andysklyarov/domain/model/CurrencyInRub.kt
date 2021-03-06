package com.andysklyarov.domain.model

import org.threeten.bp.LocalDate


data class CurrencyInRub(
    val fullName: String,
    val chCode: String,
    val date: LocalDate,
    val nom: Int,
    val value: Float,
    val diff: Float
)