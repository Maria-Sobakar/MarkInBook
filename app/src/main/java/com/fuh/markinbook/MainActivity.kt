package com.fuh.markinbook

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.fuh.markinbook.data.PushType

class MainActivity : AppCompatActivity(R.layout.activity_main) {
    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        navController = Navigation.findNavController(this, R.id.navigation_host_fragment)
        val extras = intent.extras
        if (extras != null) {
            val type = extras[KEY_TYPE]
            when (type) {
                PushType.HOMEWORK_MARK.title -> {
                    val args = Bundle().apply {
                        putString(KEY_LESSON_ID, extras[KEY_LESSON_ID] as String)
                    }
                    navController.navigate(R.id.homeworkFragment, args)
                }
                PushType.LESSON_MARK.title -> {
                    val args = Bundle().apply {
                        putString(KEY_LESSON_ID, extras[KEY_LESSON_ID] as String)
                    }
                    navController.navigate(R.id.scheduleFragment, args)
                }
                PushType.HOMEWORK_ADDED.title -> {
                    val args = Bundle().apply {
                        putString(KEY_LESSON_ID, extras[KEY_LESSON_ID] as String)

                    }
                    navController.navigate(R.id.homeworkFragment, args)

                }
            }
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        navController = Navigation.findNavController(this, R.id.navigation_host_fragment)
        val extras = intent?.extras
        if (extras != null) {
            val type = extras[KEY_TYPE]
            when (type) {
                PushType.HOMEWORK_MARK.title -> {
                    val args = Bundle().apply {
                        putString(KEY_LESSON_ID, extras[KEY_LESSON_ID] as String)
                        putString(KEY_HOMEWORK_ID, extras[KEY_HOMEWORK_ID] as String)
                        putString(KEY_DISCIPLINE_TITLE, extras[KEY_DISCIPLINE_TITLE] as String)
                    }
                    navController.navigate(R.id.homeworkFragment, args)
                }
                PushType.LESSON_MARK.title -> {
                    val args = Bundle().apply {
                        putString(KEY_LESSON_ID, extras[KEY_LESSON_ID] as String)
                        putString(KEY_DISCIPLINE_TITLE, extras[KEY_DISCIPLINE_TITLE] as String)
                    }
                    navController.navigate(R.id.scheduleFragment, args)
                }
                PushType.HOMEWORK_ADDED.title -> {
                    val args = Bundle().apply {
                        putString(KEY_LESSON_ID, extras[KEY_LESSON_ID] as String)
                        putString(KEY_HOMEWORK_ID, extras[KEY_HOMEWORK_ID] as String)
                        putString(KEY_DISCIPLINE_TITLE, extras[KEY_DISCIPLINE_TITLE] as String)
                    }
                    navController.navigate(R.id.homeworkFragment, args)

                }
            }
        }
    }

    companion object {
         const val KEY_TYPE = "type"
         const val KEY_HOMEWORK_ID = "homeworkId"
         const val KEY_LESSON_ID = "lessonId"
         const val KEY_DISCIPLINE_TITLE = "disciplineTitle"
    }
}