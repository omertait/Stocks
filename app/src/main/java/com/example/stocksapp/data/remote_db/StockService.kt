package com.example.stocksapp.data.remote_db

import com.example.stocksapp.data.model.StockData
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query


/**
 * Interface defining the endpoints for the stock service.
 */
interface StockService {

    /**
     * Retrieves the stock quote for the given symbol.
     *
     * @param symbol The symbol of the stock to fetch.
     * @param token The API token required for authentication.
     * @return A Retrofit `Response` containing the `StockData` object.
     */
    @GET("quote")
    suspend fun getQuote(
        @Query("symbol") symbol: String,
        @Query("token") token: String
    ): Response<StockData>
}