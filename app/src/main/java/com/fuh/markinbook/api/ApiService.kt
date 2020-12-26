package com.fuh.markinbook.api

import com.fuh.markinbook.data.*
import com.fuh.markinbook.data.lessons.ServerLesson
import com.fuh.markinbook.data.repository.MarkInBookServerRepository
import com.fuh.markinbook.data.Student
import okhttp3.MultipartBody
import retrofit2.http.*

interface ApiService {
    @FormUrlEncoded
    @POST("users/student/signUp")
    suspend fun signUp(
        @Field("firstName") firstName: String,
        @Field("lastName") lastName: String,
        @Field("schoolId") schoolId: Int,
        @Field("groupId") groupId: Int,
        @Field("email") email: String,
        @Field("password") password: String,
        @Tag tag: String = MarkInBookServerRepository.AUTH_TAG
    ): String

    @FormUrlEncoded
    @POST("users/tokens/add")
    suspend fun pushToken(@Field("token")token:String)

    @FormUrlEncoded
    @POST("users/student/signIn")
    suspend fun signIn(
        @Field("email") email: String,
        @Field("password") password: String,
        @Tag tag: String = MarkInBookServerRepository.AUTH_TAG
    ): String

    @GET("schools")
    suspend fun getAllSchools(@Tag tag: String = MarkInBookServerRepository.AUTH_TAG): List<School>

    @GET("schools/{schoolId}")
    suspend fun getSchoolById(@Path("schoolId") id: Int): School

    @GET("schools/{schoolId}/groups")
    suspend fun getAllGroupFromSchool(
        @Path("schoolId") id: Int,
        @Tag tag: String = MarkInBookServerRepository.AUTH_TAG
    ): List<Group>

    @GET("schools/{schoolId}/groups/{groupId}")
    suspend fun getGroupById(@Path("schoolId") schoolId: Int, @Path("groupId") groupId: Int): Group

    @GET("students/current")
    suspend fun getCurrentStudent(): Student

    @GET("lessons/by-days")
    suspend fun getLessonsForWeek(
        @Query("week") week: Int,
        @Query("year") year: Int
    ): Map<Day, MutableList<ServerLesson>>

    @Multipart
    @POST("students/current/add-profile-image")
    suspend fun addProfileImage(@Part image: MultipartBody.Part)


    @GET("lessons/{lessonId}")
    suspend fun getLesson(@Path("lessonId") lessonId: Int):ServerLesson


}