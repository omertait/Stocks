// Stock.kt
data class Stock(
    val imageResId: Int,
    val symbol: String,
    val name: String,
    val value: String,
    val change: String
)

object ItemsManager {

    val items : MutableList<Stock> = mutableListOf()

    fun add(item: Stock) {
        items.add(item)
    }

    fun remove(index:Int){
        items.removeAt(index)
    }
}
