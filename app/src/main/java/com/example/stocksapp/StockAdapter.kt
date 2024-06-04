import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.stocksapp.R

class StockAdapter(
    private val context: Context,
    private val stockList: List<Stock>

) : RecyclerView.Adapter<StockAdapter.StockViewHolder>() {

    class StockViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val stockImage: ImageView = view.findViewById(R.id.stockImage)
        val stockSymbol: TextView = view.findViewById(R.id.stockSymbol)
        val stockName: TextView = view.findViewById(R.id.stockName)
        val stockValue: TextView = view.findViewById(R.id.stockValue)
        val stockChange: TextView = view.findViewById(R.id.stockChange)
        val deleteButton: ImageButton = view.findViewById(R.id.deleteButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StockViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_stock, parent, false)
        return StockViewHolder(view)
    }

    override fun onBindViewHolder(holder: StockViewHolder, position: Int) {
        val stock = stockList[position]
        holder.stockImage.setImageResource(stock.imageResId)
        holder.stockSymbol.text = stock.symbol
        holder.stockName.text = stock.name
        holder.stockValue.text = stock.value
        holder.stockChange.text = stock.change

        val changeValue = stock.change.removeSuffix("%").toFloat()
        val colorResId = if (changeValue > 0) R.color.up else R.color.down
        val color = ContextCompat.getColor(context, colorResId)

//        holder.stockValue.setTextColor(color)
        holder.stockChange.setTextColor(color)

        holder.deleteButton.setOnClickListener {
            // Implement the deletion logic here
        }
    }

    override fun getItemCount(): Int {
        return stockList.size
    }
}
