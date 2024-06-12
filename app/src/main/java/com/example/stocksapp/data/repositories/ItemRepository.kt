package com.example.stocksapp.data.repositories

import com.example.stocksapp.data.local.ItemDao
import com.example.stocksapp.data.local.ItemsDatabase
import com.example.stocksapp.data.model.Item
import javax.inject.Inject
import android.app.Application


class ItemRepository @Inject constructor(private val itemDao: ItemDao) {

    fun getItems() = itemDao?.getItems()

    suspend fun addItem(item: Item) {
        itemDao?.addItem(item)
    }

    suspend fun deleteItem(item: Item) {
        itemDao?.deleteItem(item)
    }

    suspend fun deleteAll() {
        itemDao?.deleteAll()
    }

    suspend fun updateItem(item: Item) {
        itemDao?.updateItem(item)
    }

}