
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.stocksapp.R
import com.example.stocksapp.data.model.Item
import com.example.stocksapp.databinding.ItemStockBinding

class StockAdapter(
    private val stocks: List<Item>,
    private val itemClickListener: (Item) -> Unit,
    private val deleteClickListener: (Item) -> Unit
) : RecyclerView.Adapter<StockAdapter.ItemViewHolder>() {

    class ItemViewHolder(
        private val binding: ItemStockBinding,
        private val context : Context,
        private val itemClickListener: (Item) -> Unit,
        private val deleteClickListener: (Item) -> Unit

    ) :
            RecyclerView.ViewHolder(binding.root){

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

                    binding.root.setOnClickListener {
                        itemClickListener(item)
                    }

                    binding.deleteButton.setOnClickListener {
                        deleteClickListener(item)
                    }

                }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(ItemStockBinding.inflate(LayoutInflater.from(parent.context), parent, false), parent.context, itemClickListener, deleteClickListener)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(stocks[position])
    }

    override fun getItemCount(): Int {
        return stocks.size
    }

    fun itemAt(index: Int) = stocks[index]
}