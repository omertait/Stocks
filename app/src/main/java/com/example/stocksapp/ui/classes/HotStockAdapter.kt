package com.example.stocksapp.ui.classes

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.stocksapp.databinding.HotStockCardItemBinding

class HotStockAdapter(private val stocks: List<HotStock>) : RecyclerView.Adapter<HotStockAdapter.StockViewHolder>() {

    inner class StockViewHolder(private val binding: HotStockCardItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(stock: HotStock) {
            binding.stockImage.setImageResource(stock.imageResId)
            binding.stockName.text = stock.name
            binding.stockSymbol.text = stock.symbol
            binding.stockDescription.text = stock.description
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StockViewHolder {
        val binding = HotStockCardItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StockViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StockViewHolder, position: Int) {
        holder.bind(stocks[position % stocks.size])


    }

    override fun getItemCount(): Int = Integer.MAX_VALUE
}
