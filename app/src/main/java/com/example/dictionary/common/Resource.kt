package com.example.dictionary.common

typealias SimpleResource = Resource<Unit>

sealed class Resource<T>(val searchedData: T? = null, val data: T? = null, val message: String? = null) {
    class Loading<T>(searchedData: T? = null, data: T? = null): Resource<T>(searchedData, data)
    class Success<T>(searchedData: T? = null, data: T?): Resource<T>(searchedData, data)
    class Error<T>(message: String, data: T? = null, searchedData: T?): Resource<T>(searchedData, data, message)
}

/* sealed class Resource<T>(val data: T? = null, val message: String? = null) {
    class Loading<T>(data: T? = null): Resource<T>(data)
    class Success<T>(data: T?): Resource<T>(data)
    class Error<T>(message: String, data: T? = null): Resource<T>(data, message)
} */
