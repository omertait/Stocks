package com.example.stocksapp.ui.classes
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.stocksapp.R
import com.example.stocksapp.data.model.Item
import com.example.stocksapp.databinding.ItemStockBinding
import com.example.stocksapp.data.utils.formatWithCommas
import com.example.stocksapp.data.utils.getColor

class ItemAdapter(
    private val stocks: List<Item>,
    private val callback: ItemListener,
) : RecyclerView.Adapter<ItemAdapter.ItemViewHolder>(){

    interface ItemListener {
        fun onItemClicked(index:Int)
        fun onItemAttrClicked(index:Int)
    }
    inner class ItemViewHolder(
        private val binding: ItemStockBinding,
        private val context : Context,
    ) :
        RecyclerView.ViewHolder(binding.root), OnClickListener{

        init {
            binding.root.setOnClickListener(this)
            binding.UpdateButton.setOnClickListener{
                callback.onItemAttrClicked(adapterPosition)
            }
        }
        fun bind(item: Item){

            val todayPriceDiff =
                    item.openingPrice - item.currPrice
            val todayChange =
                    (todayPriceDiff / item.openingPrice) * 100

            binding.stockSymbol.text = item.stockSymbol
            binding.stockName.text = item.stockName
            binding.stockValue.text = "$${(item.stockAmount.toFloat() * item.currPrice.toFloat()).toString()}"
            binding.stockChange.text = "+%.2f%%".format(todayChange).formatWithCommas()

            binding.stockChange.setTextColor(getColor(todayChange, context))

            // Calculate time difference
            val currentTime = System.currentTimeMillis()
            val timeDiff = currentTime - item.lastUpdateDate
            val minutesDiff = timeDiff / (1000 * 60)

            binding.lastUpdated.text = when {
                minutesDiff < 1 -> "now"
                minutesDiff <= 30 -> "$minutesDiff min ago"
                else -> ">30 min"
            }

            Glide.with(binding.root).load(item.stockImage).circleCrop().into(binding.stockImage)


        }

        override fun onClick(p0: View?) {
            callback.onItemClicked(adapterPosition)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(ItemStockBinding.inflate(LayoutInflater.from(parent.context), parent, false), parent.context)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(stocks[position])
    }

    override fun getItemCount(): Int {
        return stocks.size
    }

    fun itemAt(index: Int) = stocks[index]

}