package hu.ait.shoppingcart.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow


@Dao
interface ShoppingDAO {
    @Query("SELECT * from shoppingtable")
    fun getAllShopping(): Flow<List<ShoppingItem>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(shoppingItem: ShoppingItem)

    @Update
    suspend fun update(shoppingItem: ShoppingItem)

    @Delete
    suspend fun delete(shoppingItem: ShoppingItem)

    @Query("DELETE from shoppingtable")
    suspend fun deleteAllShopping()
}