package com.duchastel.simon.habittracker.utils

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

fun String.parseAsLocalDate(): LocalDate? =
    try {
        LocalDate.parse(this, DateTimeFormatter.ISO_LOCAL_DATE)
    } catch(ex: DateTimeParseException) { null }