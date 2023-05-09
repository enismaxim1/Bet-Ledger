package hu.ait.shoppingcart.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import hu.ait.tododemo.R

@Entity(tableName = "bettable")
data class BetItem(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "description") val description: String,
    @ColumnInfo(name = "party1") val party1: String,
    @ColumnInfo(name = "party2") val party2: String,
    @ColumnInfo(name = "party1winamount") val party1WinAmount: String,
    @ColumnInfo(name = "party2winamount") val party2WinAmount: String,
    @ColumnInfo(name = "createdate") val createDate: String,
    @ColumnInfo(name = "resolutionstatus") val resolutionStatus: Boolean? = null
)

