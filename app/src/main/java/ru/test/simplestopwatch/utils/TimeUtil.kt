package ru.test.simplestopwatch.utils

import java.text.SimpleDateFormat
import java.util.*

const val DEFAULT_TIME_VALUE: String = "00:00:00"
const val DEFAULT_MILLISECONDS_VALUE: String = "00"
const val MAX_MILLISECONDS = 99
const val DEFAULT_TIME_UNIT_VALUE: Long = 0L
private const val EXTRA_ZERO_CONDITION_VALUE = 10
private const val TIME_PATTERN = "HH:mm:ss"

class TimeUtil {
    companion object {
        private val dateFormatter = SimpleDateFormat(TIME_PATTERN, Locale.GERMANY)

        fun getMillisecondsTextValue(value: Long): String {
            return if(value > MAX_MILLISECONDS) {
                val digit = value % 100
                getRightTimeValue(digit)
            } else {
                getRightTimeValue(value)
            }
        }

        fun getTimeUnits(totalMilliseconds: Long): String {
            val dateData = Date(totalMilliseconds)
            return dateFormatter.format(dateData)
        }

        private fun getRightTimeValue(digit: Long): String {
            return if(digit < EXTRA_ZERO_CONDITION_VALUE) {
                "0$digit"
            } else {
                "$digit"
            }
        }
    }
}