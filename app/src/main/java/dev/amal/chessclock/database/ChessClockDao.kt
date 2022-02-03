package dev.amal.chessclock.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ChessClockDao {

    @Query("SELECT * FROM chess_clock ORDER BY firstPlayerTime ASC")
    fun getAllChessClocks(): LiveData<List<ChessClock>>

    @Query("SELECT COUNT(*) FROM chess_clock")
    suspend fun count(): Int

    @Insert
    suspend fun insert(chessClock: ChessClock)

    @Insert
    suspend fun insertAll(chessClockList: List<ChessClock>)

    @Query("SELECT * FROM chess_clock WHERE id=:id")
    suspend fun get(id: Long): ChessClock

    @Query("SELECT * FROM chess_clock ORDER BY id DESC LIMIT 1")
    suspend fun getAny(): ChessClock

    @Update
    suspend fun update(chessClock: ChessClock)

    @Delete
    suspend fun delete(clock: ChessClock)

    @Query("DELETE FROM chess_clock WHERE id = :id")
    suspend fun deleteById(id: Long)
}