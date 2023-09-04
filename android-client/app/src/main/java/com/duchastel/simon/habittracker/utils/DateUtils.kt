package com.duchastel.simon.habittracker.utils

import java.time.LocalDate
import java.time.Month
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.time.temporal.WeekFields

fun String.parseAsLocalDate(): LocalDate? =
    try {
        LocalDate.parse(this, DateTimeFormatter.ISO_LOCAL_DATE)
    } catch(ex: DateTimeParseException) {
        null
    }

fun LocalDate.firstDayInWeek(weekFields: WeekFields): LocalDate =
    this.minusWeeks(1).with(weekFields.firstDayOfWeek)

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