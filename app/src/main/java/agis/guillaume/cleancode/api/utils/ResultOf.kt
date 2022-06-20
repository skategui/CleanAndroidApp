package agis.guillaume.cleancode.api.utils

import retrofit2.Response
import java.lang.Exception

/**
 * Kotlin extension functions to easily manipulate response from the backend
 */

sealed class ResultOf<out T> {
    data class Success<out R>(val value: R) : ResultOf<R>()
    data class Failure(
        val message: String? = "",
        val exception: Exception? = null
    ) : ResultOf<Nothing>()
}


inline fun <reified T> Response<T>.toResultOf(): ResultOf<T> {
    return try {
        val res = this.body()!! // will get body from request between 200 and 300
        ResultOf.Success(res)
    } catch (e: Exception) {
        // can also use the code of the request this.code()
        ResultOf.Failure(exception = e)
    }
}

// get the value inside the Success
inline fun <reified T> ResultOf<T>.getValue(): T? {
    if (this is ResultOf.Success) {
        return this.value
    }
    return null
}

// ext func to have more readable code
// ex :  res.doIfFailure { error, throwable -> DO THIS }
inline fun <reified T> ResultOf<T>.doIfFailure(callback: (error: String?, throwable: Throwable?) -> Unit) {
    if (this is ResultOf.Failure) {
        callback(message, exception)
    }
}

// ext func to have more readable code
// ex :  res.doIfSuccess { value-> DO THAT }
inline fun <reified T> ResultOf<T>.doIfSuccess(callback: (value: T) -> Unit) {
    if (this is ResultOf.Success) {
        callback(value)
    }
}