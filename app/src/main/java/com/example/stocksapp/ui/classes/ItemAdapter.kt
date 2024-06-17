package com.example.stocksapp.ui.classes
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.stocksapp.R
import com.example.stocksapp.data.model.Item
import com.example.stocksapp.data.utils.convertCurrency
import com.example.stocksapp.databinding.ItemStockBinding
import com.example.stocksapp.data.utils.formatWithCommas
import com.example.stocksapp.data.utils.getColor
import com.example.stocksapp.data.utils.getCurrencySymbol

/**
 * Adapter for displaying a list of stock items in a RecyclerView.
 *
 * @property stocks The list of stock items to display.
 * @property callback The callback interface for handling item click events.
 * @property location The location effects the conversion rates
 */
class ItemAdapter(
    private val stocks: List<Item>,
    private val callback: ItemListener,
    private var location: String
) : RecyclerView.Adapter<ItemAdapter.ItemViewHolder>(){

    /**
     * Interface for handling item click events.
     */
    interface ItemListener {
        fun onItemClicked(index:Int)
        fun onItemAttrClicked(index:Int)
    }

    /**
     * ViewHolder for holding the views of each stock item.
     *
     * @param binding The binding object for the stock item view.
     * @param context The context of the parent view group.
     */
    inner class ItemViewHolder(
        private val binding: ItemStockBinding,
        private val context : Context,
    ) :
        RecyclerView.ViewHolder(binding.root), OnClickListener{

        init {
            // Set click listeners for the root view and the update button
            binding.root.setOnClickListener(this)
            binding.UpdateButton.setOnClickListener{
                callback.onItemAttrClicked(adapterPosition)
            }
        }
        /**
         * Binds the stock item data to the views.
         *
         * @param item The stock item to bind.
         */
        fun bind(item: Item){
            val convertedCurrPrice = convertCurrency(item.currPrice, location)
            val convertedOpeningPrice = convertCurrency(item.openingPrice, location)
            val currencySymbol = getCurrencySymbol(location)

            // Calculate today's change in price and percentage
            val todayPriceDiff =
                convertedCurrPrice - convertedOpeningPrice
            val todayChange =
                    (todayPriceDiff / convertedOpeningPrice) * 100

            binding.stockSymbol.text = item.stockSymbol
            binding.stockName.text = item.stockName
            binding.stockValue.text = String.format("%s%.2f", currencySymbol, item.stockAmount.toFloat() * convertedCurrPrice.toFloat())

            // Determine the sign for positive or negative change
            val sign = if (todayChange > 0) "+" else ""
            binding.stockChange.text = "${sign}%.2f%%".format(todayChange).formatWithCommas()

            // Set text color based on positive or negative change
            binding.stockChange.setTextColor(getColor(todayChange, context))

            // Calculate time difference since last update
            val currentTime = System.currentTimeMillis()
            val timeDiff = currentTime - item.lastUpdateDate
            val minutesDiff = timeDiff / (1000 * 60)

            // Set last updated time text
            binding.lastUpdated.text = when {
                minutesDiff < 1 -> context.getString(R.string.now)
                minutesDiff <= 30 -> minutesDiff.toString().plus(" ").plus(context.getString(R.string.min_ago))
                else -> context.getString(R.string.more_than_min_ago)
            }

            // Load stock image using Glide
            Glide.with(binding.root).load(item.stockImage).circleCrop().into(binding.stockImage)


        }

        override fun onClick(p0: View?) {
            // Callback when an item is clicked
            callback.onItemClicked(adapterPosition)
        }


    }

    fun setLocation(loc : String) {
        location = loc
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        // Inflate the item view and create a new ItemViewHolder
        return ItemViewHolder(ItemStockBinding.inflate(LayoutInflater.from(parent.context), parent, false), parent.context)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        // Bind data to the ViewHolder at the specified position
        holder.bind(stocks[position])
    }

    override fun getItemCount(): Int {
        // Return the total number of items in the data set
        return stocks.size
    }

    /**
     * Retrieves the item at the specified index.
     *
     * @param index The index of the item to retrieve.
     * @return The item at the specified index.
     */
    fun itemAt(index: Int) = stocks[index]

}