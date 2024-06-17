package com.assignment.chessboard.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.assignment.chessboard.data.local.entity.KnightPath

@Dao
interface KnightPathDao {
    @Query("SELECT * FROM knightpath LIMIT 1")
    fun getLastPath(): KnightPath?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun savePath(path: KnightPath)
}