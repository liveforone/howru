package howru.howru.global.util

import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime

fun getCurrentTimestamp(): Int = Instant.now().epochSecond.toInt()

fun getDatetimeDigit(dateTime: LocalDateTime): Long {
    val year = dateTime.year.toString()
    val month = checkSingleDigit(dateTime.monthValue)
    val day = checkSingleDigit(dateTime.dayOfMonth)
    val hour = checkSingleDigit(dateTime.hour)
    val minute = checkSingleDigit(dateTime.minute)

    return "$year$month$day$hour$minute".toLong()
}

fun getDateDigit(date: LocalDate): Int {
    val year = date.year.toString()
    val month = checkSingleDigit(date.monthValue)
    val day = checkSingleDigit(date.dayOfMonth)

    return "$year$month$day".toInt()
}

fun convertDateToLocalDate(date: Int): LocalDate {
    val stringDate = date.toString()
    val year = stringDate.substring(0, 4).toInt()
    val month = stringDate.substring(4, 6).toInt()
    val day = stringDate.substring(6).toInt()

    return LocalDate.of(year, month, day)
}

private fun checkSingleDigit(digit: Int): String =
    if (digit in 0..9) {
        "0$digit"
    } else {
        digit.toString()
    }
