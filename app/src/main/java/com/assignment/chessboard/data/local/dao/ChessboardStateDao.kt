package com.assignment.chessboard.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.assignment.chessboard.data.local.entity.ChessboardState

@Dao
interface ChessboardStateDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveState(chessboardState: ChessboardState)

    @Query("SELECT * FROM chessboard_state ORDER BY id DESC LIMIT 1")
    suspend fun getLastState(): ChessboardState?
}
