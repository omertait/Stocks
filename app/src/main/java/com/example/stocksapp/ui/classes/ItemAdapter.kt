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

class ItemAdapter(
    private val stocks: List<Item>,
    private val callback: ItemListener
) : RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {

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
                    binding.stockSymbol.text = item.stockSymbol
                    binding.stockName.text = item.stockName
//                    update to relevant values
                    binding.stockValue.text = item.stockPrice.toString()
//                    binding.stockImage.setImageResource(item.imageResId)
//                    val changeValue = item.stockPrice.removeSuffix("%").toFloat()
                    val changeValue = item.stockPrice.toFloat()
                    val sign = if (changeValue > 0) "+" else "-"
                    binding.stockChange.text = "${sign}${item.stockAmount.toString()}%"

                    val colorResId = if (changeValue > 0) R.color.up else R.color.down
                    val color = ContextCompat.getColor(context, colorResId)
                    binding.stockChange.setTextColor(color)

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