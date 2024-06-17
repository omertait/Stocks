package com.example.stocksapp.data.remote_db

import com.example.stocksapp.data.model.StockData
import com.example.stocksapp.data.utils.Resource
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Remote data source for fetching stock data from a remote service.
 *
 * @property stockService The service used for making network calls.
 */
@Singleton
class StockRemoteDataSource @Inject constructor(
    private val stockService: StockService
) : BaseDataSource() {

    /**
     * Fetches the stock quote for the given symbol.
     *
     * @param symbol The symbol of the stock to fetch.
     * @param token The API token required for authentication.
     * @return A `Resource` object containing the result of the network call.
     */
    suspend fun getQuote(symbol: String, token: String): Resource<StockData> {
        // Call the base class method to execute the network call and return the result
        return getResult { stockService.getQuote(symbol, token) }
    }
}