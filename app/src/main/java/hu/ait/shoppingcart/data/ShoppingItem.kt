package hu.ait.shoppingcart.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import hu.ait.tododemo.R

@Entity(tableName = "shoppingtable")
data class ShoppingItem(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "description") val description: String,
    @ColumnInfo(name = "estimatedprice") val estimatedPrice: String,
    @ColumnInfo(name = "createdate") val createDate: String,
    @ColumnInfo(name = "category") var category: ShoppingCategory,
    @ColumnInfo(name = "status") var status: Boolean
)

enum class ShoppingCategory {
    FOOD, ELECTRONICS, BOOK;

    fun getIcon(): Int {
        // The this is the value of this enum object
        return when (this) {
            FOOD -> R.drawable.food
            ELECTRONICS -> R.drawable.electronics
            BOOK -> R.drawable.book
        }
    }

}