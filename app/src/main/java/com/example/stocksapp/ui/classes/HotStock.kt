package com.example.stocksapp.ui.classes
/**
 * Data class representing a hot stock item.
 *
 * @property imageResId The resource ID of the stock image.
 * @property symbol The symbol of the stock.
 * @property name The name of the stock.
 * @property description The description of the stock.
 */
data class HotStock(
    val imageResId: Int,
    val symbol: String,
    val name: String,
    val description: String,
)