package com.example.stocksapp.data.remote_db

import com.example.stocksapp.data.utils.Resource
import retrofit2.Response

/**
 * Abstract class that handles network responses and transforms them into Resource objects.
 */
abstract class BaseDataSource {

    /**
     * Executes the network call and returns a Resource object containing the result.
     *
     * @param call the suspend function that makes the network call.
     * @return a Resource object containing the result of the network call.
     */
    protected suspend fun <T> getResult(call: suspend () -> Response<T>): Resource<T> {
        try {
            // Execute the network call
            val result = call()

            // Check if the result is successful
            if (result.isSuccessful) {
                val body = result.body()
                if (body != null) {
                    // Return the body wrapped in a success Resource
                    return Resource.success(body)
                }
            }

            // Return an error Resource with the error message and code
            return Resource.error(
                "Network call has failed for the following reason: ${result.message()} ${result.code()}"
            )
        } catch (e: Exception) {
            // Catch and return an error Resource with the exception message
            return Resource.error(
                "Network call has failed for the following reason: " + (e.localizedMessage ?: e.toString())
            )
        }
    }
}