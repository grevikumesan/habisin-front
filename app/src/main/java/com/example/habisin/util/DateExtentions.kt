package com.example.habisin.util

import java.util.Calendar
import java.util.Date
import java.util.concurrent.TimeUnit

fun Date.daysFromToday(): Int {
    val today = Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }.timeInMillis

    val target = Calendar.getInstance().apply {
        time = this@daysFromToday
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }.timeInMillis

    val diff = target - today
    return TimeUnit.MILLISECONDS.toDays(diff).toInt()
}