package com.example.stocksapp.data.remote_db

import com.example.stocksapp.data.model.StockData
import com.example.stocksapp.data.utils.Resource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StockRemoteDataSource @Inject constructor(
    private val stockService: StockService
) : BaseDataSource() {

    suspend fun getQuote(symbol: String, token: String): Resource<StockData> {
        return getResult { stockService.getQuote(symbol, token) }
    }
}