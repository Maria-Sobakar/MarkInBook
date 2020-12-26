package com.fuh.markinbook.data.lessons

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class LocalLesson(
    val discipline:String,
    @PrimaryKey override val start: Long,
    val durationInMinutes: Int,
    val homework: String,
):Lesson {
}