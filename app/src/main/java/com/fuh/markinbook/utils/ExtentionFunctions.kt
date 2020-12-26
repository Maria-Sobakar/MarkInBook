package com.fuh.markinbook.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkInfo
import android.util.Log
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.net.ConnectivityManagerCompat
import com.fuh.markinbook.R
import com.fuh.markinbook.data.Day
import com.fuh.markinbook.data.lessons.LocalLesson
import java.util.*


fun dayName(context: Context, day: Day): String {
    return when (day) {
        Day.MONDAY -> context.getString(R.string.monday)
        Day.TUESDAY -> context.getString(R.string.tuesday)
        Day.WEDNESDAY -> context.getString(R.string.wednesday)
        Day.THURSDAY -> context.getString(R.string.thursday)
        Day.FRIDAY -> context.getString(R.string.friday)
        Day.SATURDAY -> context.getString(R.string.saturday)
        Day.SUNDAY -> context.getString(R.string.sunday)
    }
}

fun isEmailValid(email: String): Boolean {
    return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
}

fun Calendar.addDuration(duration: Int): Long {
    val durationInMillis = duration * 60000
    return timeInMillis + durationInMillis
}

fun inflater(context: Context): LayoutInflater = LayoutInflater.from(context)

fun Calendar.getMinutes(): String {
    val minutes = get(Calendar.MINUTE)
    return if (minutes <= 9) {
        "0$minutes"
    } else {
        "$minutes"
    }
}

fun getMillisFromCalendars(date: Calendar, time: Calendar): Long {
    val target = Calendar.getInstance()
    target.set(
        date.get(Calendar.YEAR),
        date.get(Calendar.MONTH),
        date.get(Calendar.DAY_OF_MONTH),
        time.get(Calendar.HOUR_OF_DAY),
        time.get(Calendar.MINUTE),
        0
    )
    return target.timeInMillis
}


fun LocalLesson.getDay(): Day {
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = start
    val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
    return when (dayOfWeek) {
        Calendar.MONDAY -> Day.MONDAY
        Calendar.TUESDAY -> Day.TUESDAY
        Calendar.WEDNESDAY -> Day.WEDNESDAY
        Calendar.THURSDAY -> Day.THURSDAY
        Calendar.FRIDAY -> Day.FRIDAY
        Calendar.SATURDAY -> Day.SATURDAY
        else -> Day.SUNDAY
    }
}

fun LocalLesson.isThisWeek(current: Int): Boolean {
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = start
    val week = calendar.get(Calendar.WEEK_OF_YEAR)
    return week == current
}



