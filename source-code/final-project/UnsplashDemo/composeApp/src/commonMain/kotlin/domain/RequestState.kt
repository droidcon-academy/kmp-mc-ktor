package domain

import kotlinx.serialization.Serializable

@Serializable
sealed class RequestState<out T> {
    @Serializable
    data object Idle : RequestState<Nothing>()

    @Serializable
    data object Loading : RequestState<Nothing>()

    @Serializable
    data class Success<out T>(val data: T) : RequestState<T>()

    @Serializable
    data class Error(val message: String) : RequestState<Nothing>()

    fun isLoading(): Boolean = this is Loading
    fun isSuccess(): Boolean = this is Success
    fun isError(): Boolean = this is Error

    fun getSuccessData() = (this as Success).data
    fun getErrorMessage(): String = (this as Error).message
}
