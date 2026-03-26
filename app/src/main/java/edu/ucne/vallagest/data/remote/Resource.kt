package edu.ucne.vallagest.data.remote

sealed class Resource <T>(
    val data: T? = null,
    val message: String? = null
) {
    class Succes<T>(data: T) : Resource<T>(data)
    class Error<T>(message: String) : Resource<T>(null, message)
    class Loading<T> : Resource<T>()
}