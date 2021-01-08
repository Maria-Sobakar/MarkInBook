package com.fuh.markinbook.data.repository

import com.fuh.markinbook.PreferencesManager
import com.fuh.markinbook.api.ApiService
import com.fuh.markinbook.api.ResultWrapper
import com.fuh.markinbook.data.Day
import com.fuh.markinbook.data.Group
import com.fuh.markinbook.data.School
import com.fuh.markinbook.data.lessons.ServerLesson
import com.google.gson.GsonBuilder
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Path
import timber.log.Timber
import java.io.IOException


class MarkInBookServerRepository(private val dispatcher: CoroutineDispatcher = Dispatchers.IO) {
    private val service: ApiService

    init {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        val httpClient = OkHttpClient.Builder()
        httpClient.addInterceptor(logging)
        httpClient.addInterceptor {
            val original = it.request()
            val requestBuilder = original.newBuilder()
            if (original.tag(String::class.java) != AUTH_TAG) {
                requestBuilder.addHeader("Authorization", "Bearer ${PreferencesManager.userToken}")
            }

            val request = requestBuilder.build()
            it.proceed(request)
        }
        val gson = GsonBuilder()
            .setLenient()
            .serializeNulls()
            .serializeSpecialFloatingPointValues()
            .create()
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(httpClient.build())
            .build()
        service = retrofit.create(ApiService::class.java)
    }

    suspend fun signIn(
        email: String,
        password: String,
    ): ResultWrapper<String> {
        return safeApiCall(dispatcher) { service.signIn(email, password) }
    }

    suspend fun signUp(
        firstName: String,
        lastName: String,
        schoolId: Int,
        groupId: Int,
        email: String,
        password: String,
    ): ResultWrapper<String> {
        return safeApiCall(dispatcher) {
            service.signUp(
                firstName,
                lastName,
                schoolId,
                groupId,
                email,
                password
            )
        }
    }

    suspend fun getAllSchools():ResultWrapper<List<School>> {
        return safeApiCall(dispatcher){
            service.getAllSchools()
        }
    }

    suspend fun getSchoolById(schoolId: Int) = service.getSchoolById(schoolId)

    suspend fun getLessonsForWeek(
        week: Int,
        year: Int
    ): ResultWrapper<Map<Day, MutableList<ServerLesson>>> {
        return safeApiCall(dispatcher) {
            service.getLessonsForWeek(week, year)
        }
    }

    suspend fun getGroups(id: Int):ResultWrapper<List<Group>> {
        return safeApiCall(dispatcher) {
            service.getAllGroupFromSchool(id)
        }
    }
        suspend fun getGroupById(schoolId: Int, groupId: Int) = service.getGroupById(schoolId, groupId)

        suspend fun getCurrentStudent() = service.getCurrentStudent()

        suspend fun addProfileImage(image: MultipartBody.Part) = service.addProfileImage(image)

        suspend fun getLesson(lessonId: Int) = service.getLesson(lessonId)

        suspend fun pushToken(token:String) = service.pushToken(token)

        private suspend fun <T> safeApiCall(
            dispatcher: CoroutineDispatcher,
            apiCall: suspend () -> T
        ): ResultWrapper<T> {
            return withContext(dispatcher) {
                try {
                    ResultWrapper.Success(apiCall())
                } catch (exception: Exception) {
                    Timber.e(exception)
                    when (exception) {
                        is IOException -> {
                            ResultWrapper.NetworkError(exception)
                        }
                        is HttpException -> {
                            ResultWrapper.GenericError(exception.code())
                        }
                        else -> {
                            ResultWrapper.GenericError()
                        }
                    }
                }
            }
        }

        companion object {
        const val BASE_URL = "http://0.0.0.0:8081/api/v1/"
        const val AUTH_TAG = "Auth_tag"
    }
    }
