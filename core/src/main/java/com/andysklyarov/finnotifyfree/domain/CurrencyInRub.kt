package com.andysklyarov.finnotifyfree.domain

import java.time.LocalDate

data class CurrencyInRub (val name: CurrencyName, val date: LocalDate, val denomination: Int, val value: Float)