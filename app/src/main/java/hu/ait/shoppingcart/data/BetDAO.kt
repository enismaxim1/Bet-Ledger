package hu.ait.shoppingcart.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow


@Dao
interface BetDAO {
    @Query("SELECT * from bettable")
    fun getAllBets(): Flow<List<BetItem>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(betItem: BetItem)

    @Update
    suspend fun update(betItem: BetItem)

    @Delete
    suspend fun delete(betItem: BetItem)

    @Query("DELETE from bettable")
    suspend fun deleteAllBets()
}