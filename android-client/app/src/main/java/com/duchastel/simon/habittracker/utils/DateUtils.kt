package com.duchastel.simon.habittracker.utils

import java.time.LocalDate
import java.time.Month
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

fun String.parseAsLocalDate(): LocalDate? =
    try {
        LocalDate.parse(this, DateTimeFormatter.ISO_LOCAL_DATE)
    } catch(ex: DateTimeParseException) {
        null
    }

fun Month.asString() = when (this) {
    Month.JANUARY -> "January"
    Month.FEBRUARY -> "February"
    Month.MARCH -> "March"
    Month.APRIL -> "April"
    Month.MAY -> "May"
    Month.JUNE -> "June"
    Month.JULY -> "July"
    Month.AUGUST -> "August"
    Month.SEPTEMBER -> "September"
    Month.OCTOBER -> "October"
    Month.NOVEMBER -> "November"
    Month.DECEMBER -> "December"
}