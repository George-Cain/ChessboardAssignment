package com.assignment.chessboard.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "chessboard_state")
data class ChessboardState(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val boardSize: Int,
    val startX: Int,
    val startY: Int,
    val endX: Int,
    val endY: Int,
    val maxMoves: Int,
    val pathJson: String? = null,
    val isDarkTheme: Boolean = false
)
