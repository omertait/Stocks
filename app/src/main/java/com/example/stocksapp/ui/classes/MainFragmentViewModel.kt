package com.example.stocksapp.ui.classes

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import android.app.Application
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import com.example.stocksapp.data.model.Item
import com.example.stocksapp.data.repositories.ItemRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the MainFragment.
 *
 * @property repository The repository for handling data operations.
 */
@HiltViewModel
class MainFragmentViewModel @Inject constructor(private val repository: ItemRepository) : ViewModel() {

    // LiveData for observing the list of items
    val items: LiveData<List<Item>>? = repository.getItems()

    // MutableLiveData for observing the chosen item
    private val _chosenItem = MutableLiveData<Item>()
    val chosenItem: LiveData<Item> get() = _chosenItem

    /**
     * Sets the chosen item in the ViewModel.
     *
     * @param item The item to set as chosen.
     */
    fun setItem(item: Item) {
        _chosenItem.value = item
    }

    /**
     * Adds an item to the database.
     *
     * @param item The item to add.
     */
    fun addItem(item: Item) {
        viewModelScope.launch {
            repository.addItem(item)
        }
    }

    /**
     * Deletes an item from the database.
     *
     * @param item The item to delete.
     */
    fun deleteItem(item: Item) {
        viewModelScope.launch {
            repository.deleteItem(item)
        }
    }

    /**
     * Deletes all items from the database.
     */
    fun deleteAll() {
        viewModelScope.launch {
            repository.deleteAll()
        }
    }

    /**
     * Updates an item in the database.
     *
     * @param item The item to update.
     */
    fun updateItem(item: Item) {
        viewModelScope.launch {
            repository.updateItem(item)
        }
    }
}