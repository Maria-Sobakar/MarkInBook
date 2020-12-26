package com.fuh.markinbook.api

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

sealed class ResultWrapper<out T> {
    data class Success<out T>(val value: T) : ResultWrapper<T>()
    data class GenericError(val code: Int? = null) : ResultWrapper<Nothing>()
    data class NetworkError (val ex: Throwable?=null): ResultWrapper<Nothing>()


}