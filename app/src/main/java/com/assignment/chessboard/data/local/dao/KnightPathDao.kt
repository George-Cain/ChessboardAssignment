package com.assignment.chessboard.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.assignment.chessboard.data.local.entity.KnightPath

@Dao
interface KnightPathDao {
    @Insert
    suspend fun savePath(knightPath: KnightPath)

    @Query("SELECT * FROM knight_path ORDER BY id DESC LIMIT 1")
    suspend fun getLastPath(): KnightPath?
}
