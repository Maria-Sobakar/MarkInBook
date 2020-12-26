package com.fuh.markinbook.data.database

import android.content.Context
import androidx.room.Room

object DatabaseBuilder {
    private const val DATABASE_NAME = "markInBook-database"
    lateinit var instance: MarkInBookDatabase

    fun initialize(context: Context) {
        synchronized(MarkInBookDatabase::class.java) {
            instance = buildRoomDB(context)
        }
    }

    private fun buildRoomDB(context: Context) =
        Room.databaseBuilder(
            context.applicationContext,
            MarkInBookDatabase::class.java,
            DATABASE_NAME
        ).build()

}