package hu.ait.betledger.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

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
    @ColumnInfo(name = "resolutionstatus") val resolutionStatus: ResolutionStatus,
)
enum class ResolutionStatus {
    UNRESOLVED,
    PARTY1_WIN,
    PARTY2_WIN
}

