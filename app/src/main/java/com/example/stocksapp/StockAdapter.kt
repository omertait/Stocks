
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.stocksapp.R
import com.example.stocksapp.databinding.ItemStockBinding

class StockAdapter(
    private val stocks: List<Stock>,
    private val itemClickListener: (Stock) -> Unit
) : RecyclerView.Adapter<StockAdapter.ItemViewHolder>() {

    class ItemViewHolder(
        private val binding: ItemStockBinding,
        private val context : Context,
        private val itemClickListener: (Stock) -> Unit
    ) :
            RecyclerView.ViewHolder(binding.root){

                fun bind(item : Stock){
                    binding.stockSymbol.text = item.symbol
                    binding.stockName.text = item.name
                    binding.stockValue.text = item.value
                    binding.stockChange.text = item.change
//                    binding.stockImage.setImageResource(item.imageResId)
                    val changeValue = item.change.removeSuffix("%").toFloat()
                    val colorResId = if (changeValue > 0) R.color.up else R.color.down
                    val color = ContextCompat.getColor(context, colorResId)
                    binding.stockChange.setTextColor(color)

                    binding.root.setOnClickListener {
                        itemClickListener(item)
                    }

                }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(ItemStockBinding.inflate(LayoutInflater.from(parent.context), parent, false), parent.context, itemClickListener)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(stocks[position])
    }

    override fun getItemCount(): Int {
        return stocks.size
    }
}