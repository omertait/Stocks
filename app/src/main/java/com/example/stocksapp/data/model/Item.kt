package com.example.stocksapp.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "items_table")
data class Item(
//    @ColumnInfo(name = "stockName")
//    val stockName: String,

    @ColumnInfo(name = "stockSymbol")
    val stockSymbol: String,

    @ColumnInfo(name = "stockPrice")
    val stockPrice: Double,

    @ColumnInfo(name = "stockAmount")
    val stockAmount: Long,

    @ColumnInfo(name = "stockImage")
    val stockImage: String?,

    @ColumnInfo(name = "currPrice")
    var currPrice: Double,

) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}

