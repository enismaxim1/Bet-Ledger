package hu.ait.betledger.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow


@Dao
interface BetDAO {
    @Query("SELECT * from bettable")
    fun getAllBets(): Flow<List<BetItem>>

    @Query(
        "SELECT * from bettable WHERE " +
                "((:query IS NULL OR (title LIKE '%' || :query || '%')" +
                "OR (description LIKE '%' || :query || '%'))" +
                " AND (:party1 IS NULL OR (party1 = :party1 OR party2 = :party1))" +
                " AND (:party2 IS NULL OR (party1 = :party2 OR party2 = :party2))" +
                " AND (:before IS NULL OR (createdate < :before))" +
                " AND (:after IS NULL OR (createdate > :after))" +
                " AND (:status IS NULL OR (resolutionstatus = :status)))"
    )
    fun getAllBetsFiltered(
        query: String?, party1: String?, party2: String?, before: String?,
        after: String?, status: ResolutionStatus?
    ): Flow<List<BetItem>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(betItem: BetItem)

    @Update
    suspend fun update(betItem: BetItem)

    @Delete
    suspend fun delete(betItem: BetItem)

    @Query("DELETE from bettable")
    suspend fun deleteAllBets()
}