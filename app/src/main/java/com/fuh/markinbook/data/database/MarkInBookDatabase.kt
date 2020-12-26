package com.fuh.markinbook.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.fuh.markinbook.data.lessons.LocalLesson


@Database(entities = [LocalLesson::class], version = 1)

abstract class MarkInBookDatabase: RoomDatabase() {
    abstract fun markInBookDAO(): MarkInBookDAO
}

