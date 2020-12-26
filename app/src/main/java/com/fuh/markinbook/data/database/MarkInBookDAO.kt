package com.fuh.markinbook.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.fuh.markinbook.data.Discipline
import com.fuh.markinbook.data.lessons.LocalLesson
import com.fuh.markinbook.data.lessons.ServerLesson
@Dao
interface MarkInBookDAO {

    @Insert
    suspend fun addLesson(lesson: LocalLesson)

    @Query("SELECT * FROM locallesson ORDER BY start ASC")
    suspend fun getLessons(): List<LocalLesson>

    @Query("SELECT discipline FROM locallesson")
    suspend fun getDisciplines():List<String>
}