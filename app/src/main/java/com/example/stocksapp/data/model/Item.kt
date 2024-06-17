package com.example.stocksapp.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Represents an item in the items_table.
 */
@Entity(tableName = "items_table")
data class Item(

    /**
     * The name of the stock.
     */
    @ColumnInfo(name = "stockName")
    var stockName: String,

    /**
     * The symbol of the stock.
     */
    @ColumnInfo(name = "stockSymbol")
    var stockSymbol: String,

    /**
     * The price of the stock.
     */
    @ColumnInfo(name = "stockPrice")
    var stockPrice: Double,

    /**
     * The amount of stock.
     */
    @ColumnInfo(name = "stockAmount")
    var stockAmount: Long,

    /**
     * The URL or path of the stock image.
     * This field is optional and can be null.
     */
    @ColumnInfo(name = "stockImage")
    var stockImage: String?,

    /**
     * The current price of the stock.
     */
    @ColumnInfo(name = "currPrice")
    var currPrice: Double,

    /**
     * The opening price of the stock.
     */
    @ColumnInfo(name = "openingPrice")
    var openingPrice: Double,

    /**
     * The last update date in milliseconds since epoch.
     */
    @ColumnInfo(name = "lastUpdateDate")
    var lastUpdateDate: Long,
) {
    /**
     * The primary key for the item. This is auto-generated.
     */
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}

