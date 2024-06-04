package com.example.stocksapp.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.stoks.data.model.Item


@Dao
interface ItemDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addItem(item: Item)

    @Delete
    suspend fun deleteItem(vararg item: Item)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateItem(item: Item)


    @Query("SELECT * from items_table ORDER BY stockSymbol ASC")
    fun getItems(): LiveData<List<Item>>

    @Query("SELECT * from items_table WHERE stockSymbol LIKE:title")
    suspend fun getItem(title: String): Item


    @Query("DELETE from items_table")
    suspend fun deleteAll()

//    @Query("UPDATE items_table SET currPrice = :newPrice WHERE stockName = :stockName")
//    suspend fun updateCurrentPrice(stockName: String, newPrice: Double)
//
//    @Query("SELECT SUM(stockAmount) FROM items_table WHERE stockName LIKE:stockName")
//    fun getTotalAmountForStockFlow(stockName: String): LiveData<Int>


}

