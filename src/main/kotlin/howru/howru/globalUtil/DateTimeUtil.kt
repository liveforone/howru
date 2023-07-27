package howru.howru.globalUtil

import java.time.Instant
import java.time.LocalDateTime

const val DATE_TYPE = "BIGINT(12)"

fun getCurrentTimestamp(): Int {
    return Instant.now().epochSecond.toInt()
}

fun datetimeConvertToDigit(): Long {
    val now = LocalDateTime.now()
    val year = now.year.toString()
    val month = checkSingleDigit(now.monthValue)
    val day = checkSingleDigit(now.dayOfMonth)
    val hour = checkSingleDigit(now.hour)
    val minute = checkSingleDigit(now.minute)

    return "$year$month$day$hour$minute".toLong()
}

private fun checkSingleDigit(digit: Int): String {
    return if (digit in 0..9) {
        "0${digit}"
    } else {
        digit.toString()
    }
}