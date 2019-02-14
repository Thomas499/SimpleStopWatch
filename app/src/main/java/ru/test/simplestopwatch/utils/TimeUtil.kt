package ru.test.simplestopwatch.utils

import java.lang.IllegalArgumentException
import java.text.SimpleDateFormat
import java.util.*

const val DEFAULT_TIME_VALUE: String = "00"
const val MAX_MILLISECONDS = 99
const val DEFAULT_TIME_UNIT_VALUE: Long = 0L
const val SECONDS_KEY = "Seconds"
const val MINUTES_KEY = "Minutes"
const val HOURS_KEY = "Hours"
private const val EXTRA_ZERO_CONDITION_VALUE = 10
private const val SECONDS_PATTERN = "ss"
private const val MINUTES_PATTERN = "mm"
private const val HOURS_PATTERN = "HH"

class TimeUtil {
    companion object {
        private val dateFormatter = SimpleDateFormat(SECONDS_PATTERN, Locale.GERMANY)

        fun getMillisecondsTextValue(value: Long): String {
            return if(value > MAX_MILLISECONDS) {
                val digit = value % 100
                getRightTimeValue(digit)
            } else {
                getRightTimeValue(value)
            }
        }

        fun getTimeUnits(totalMilliseconds: Long, timeUnit: String = SECONDS_KEY): String {
            val dateData = Date(totalMilliseconds)

            fun getTime(timePattern: String): String {
                dateFormatter.applyPattern(timePattern)
                return dateFormatter.format(dateData)
            }

            return when(timeUnit) {
                SECONDS_KEY -> getTime(SECONDS_PATTERN)
                MINUTES_KEY -> getTime(MINUTES_PATTERN)
                HOURS_KEY -> getTime(HOURS_PATTERN)
                else -> throw IllegalArgumentException("Wrong time unit")
            }
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