package com.andysklyarov.data.repository

import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter


const val INPUT_DATE_TIME_PATTERN = "yyyy-MM-dd"
const val TIME_PATTERN = "T00:00:00"
const val OUTPUT_DATE_TIME_PATTERN = "yyyy-MM-dd'T'HH:mm:ss"
const val INTERNAL_DATE_PATTERN = "yyyyMMdd"


fun getInputDateTimeString(date: LocalDate): String {
    return getFormattedDateString(date, INPUT_DATE_TIME_PATTERN) + TIME_PATTERN
}

fun getFormattedDateString(date: LocalDate, format: String): String {
    return DateTimeFormatter.ofPattern(format).format(date)
}