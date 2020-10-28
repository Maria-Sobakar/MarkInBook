package com.fuh.markinbook.data.dto

import androidx.room.Entity
import java.lang.StringBuilder
import java.util.*

@Entity
data class Lesson(
    val lessonId:UUID = UUID.randomUUID(),
    val dayOfWeek: Day,
    val startTime: Int,
    val duration: Int = 45,
    var lessonName: String,
    var mark: Int? = null
) : Item {
    fun lessonTime() =
        "${startTime / 60}:${startTime % 60}-${(startTime + duration) / 60}:${(startTime + duration) % 60}"
}

