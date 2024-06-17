package com.assignment.chessboard.domain.utils

import android.util.Log

fun knightPathFinder(boardSize: Int, startX: Int, startY: Int, endX: Int, endY: Int, maxMoves: Int): List<List<Pair<Int, Int>>> {
    val directions = listOf(
        Pair(2, 1), Pair(2, -1), Pair(-2, 1), Pair(-2, -1),
        Pair(1, 2), Pair(1, -2), Pair(-1, 2), Pair(-1, -2)
    )

    val queue = ArrayDeque<KnightMove>()
    queue.add(KnightMove(startX, startY, listOf(Pair(startX, startY))))
    val results = mutableListOf<List<Pair<Int, Int>>>()

    while (queue.isNotEmpty()) {
        val current = queue.removeFirst()
        if (current.path.size > maxMoves + 1) continue
        if (current.x == endX && current.y == endY && current.path.size <= maxMoves + 1) {
            results.add(current.path)
            Log.d("KnightPathFinder", "Found path: $current.path")
            continue
        }

        for (direction in directions) {
            val newX = current.x + direction.first
            val newY = current.y + direction.second
            if (newX in 0 until boardSize && newY in 0 until boardSize) {
                val newPath = current.path + Pair(newX, newY)
                queue.add(KnightMove(newX, newY, newPath))
            }
        }
    }

    Log.d("KnightPathFinder", "Total paths found: ${results.size}")
    return results
}
