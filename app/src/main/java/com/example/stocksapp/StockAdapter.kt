// StockAdapter.kt
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.stocksapp.R

class StockAdapter(private val context: Context, private val stockList: List<Stock>) : BaseAdapter() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)

    override fun getCount(): Int {
        return stockList.size
    }

    override fun getItem(position: Int): Any {
        return stockList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View = convertView ?: inflater.inflate(R.layout.item_stock, parent, false)

        val stockImage: ImageView = view.findViewById(R.id.stockImage)
        val stockSymbol: TextView = view.findViewById(R.id.stockSymbol)
        val stockName: TextView = view.findViewById(R.id.stockName)
        val stockValue: TextView = view.findViewById(R.id.stockValue)
        val stockChange: TextView = view.findViewById(R.id.stockChange)
        val deleteButton: ImageButton = view.findViewById(R.id.deleteButton)

        val stock = stockList[position]

        stockImage.setImageResource(stock.imageResId)
        stockSymbol.text = stock.symbol
        stockName.text = stock.name
        stockValue.text = stock.value
        stockChange.text = stock.change

        val changeValue = stock.change.removeSuffix("%").toFloat()
        val colorResId = if (changeValue > 0) R.color.up else R.color.down
        val color = ContextCompat.getColor(context, colorResId)

        stockChange.setTextColor(color)

        deleteButton.setOnClickListener {
            // Implement the deletion logic here
        }

        return view
    }
}
