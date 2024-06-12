package com.example.stocksapp.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "items_table")
data class Item(
    @ColumnInfo(name = "stockName")
    var stockName: String,

    @ColumnInfo(name = "stockSymbol")
    var stockSymbol: String,

    @ColumnInfo(name = "stockPrice")
    var stockPrice: Double,

    @ColumnInfo(name = "stockAmount")
    var stockAmount: Long,

    @ColumnInfo(name = "stockImage")
    var stockImage: String?,

    @ColumnInfo(name = "currPrice")
    var currPrice: Double,

    @ColumnInfo(name = "openingPrice")
    var openingPrice: Double,

    @ColumnInfo(name = "lastUpdateDate")
    var lastUpdateDate: Long,

    ) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}

