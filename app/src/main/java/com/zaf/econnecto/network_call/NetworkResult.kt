package com.zaf.econnecto.network_call
// implement this for Api call from https://medium.com/codex/kotlin-sealed-classes-for-better-handling-of-api-response-6aa1fbd23c76

sealed class NetworkResult<T> (val data: T? = null, val message: String? = null) {

    class Success<T>(data: T?) : NetworkResult<T> (data)
    class Error<T> (message: String?, data: T? = null): NetworkResult<T>(data,message)
    class Loading<T>: NetworkResult<T>()

}