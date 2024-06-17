package com.example.stocksapp.ui.classes

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.stocksapp.databinding.HotStockCardItemBinding

/**
 * Adapter for displaying a list of hot stocks in a RecyclerView.
 *
 * @property stocks The list of hot stocks to display.
 */
class HotStockAdapter(private val stocks: List<HotStock>) : RecyclerView.Adapter<HotStockAdapter.StockViewHolder>() {

    /**
     * ViewHolder for holding the views of each hot stock item.
     *
     * @param binding The binding object for the hot stock item view.
     */
    inner class StockViewHolder(private val binding: HotStockCardItemBinding) : RecyclerView.ViewHolder(binding.root) {
        /**
         * Binds the hot stock data to the views.
         *
         * @param stock The hot stock item to bind.
         */
        fun bind(stock: HotStock) {
            binding.stockName.text = stock.name
            binding.stockSymbol.text = stock.symbol
            binding.stockDescription.text = stock.description
            // Load stock image using Glide
            Glide.with(binding.root).load(stock.imageResId).into(binding.stockImage)
        }
    }

    /**
     * Creates and returns a new StockViewHolder.
     *
     * @param parent The parent view group.
     * @param viewType The view type.
     * @return The created StockViewHolder.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StockViewHolder {
        val binding = HotStockCardItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StockViewHolder(binding)
    }

    /**
     * Binds the data at the specified position to the StockViewHolder.
     * For infinite scrolling, we go to the correct position using %
     *
     * @param holder The StockViewHolder to bind data to.
     * @param position The position of the data to bind.
     */
    override fun onBindViewHolder(holder: StockViewHolder, position: Int) {
        holder.bind(stocks[position % stocks.size])


    }

    /**
     * Returns the total number of items in the data set, which is set to Integer.MAX_VALUE for infinite scrolling.
     *
     * @return The total number of items.
     */
    override fun getItemCount(): Int = Integer.MAX_VALUE
}
