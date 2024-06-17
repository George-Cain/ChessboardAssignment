package com.assignment.chessboard.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class KnightPath(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val boardSize: Int,
    val startX: Int,
    val startY: Int,
    val endX: Int,
    val endY: Int,
    val maxMoves: Int
)