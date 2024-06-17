package com.assignment.chessboard.presentation.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.assignment.chessboard.data.local.dao.KnightPathDao
import com.assignment.chessboard.data.local.database.AppDatabase
import com.assignment.chessboard.data.local.entity.KnightPath
import com.assignment.chessboard.domain.utils.knightPathFinder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ChessboardViewModel(application: Application) : AndroidViewModel(application) {
    private val knightPathDao: KnightPathDao = AppDatabase.getDatabase(application).knightPathDao()

    val _boardSize = MutableLiveData(8)
    val boardSize: LiveData<Int> get() = _boardSize

    val _maxMoves = MutableLiveData(3)
    val maxMoves: LiveData<Int> get() = _maxMoves

    private val _paths = MutableLiveData<List<List<Pair<Int, Int>>>>(emptyList())
    val paths: LiveData<List<List<Pair<Int, Int>>>> get() = _paths

    private val _noSolutionFound = MutableLiveData(false)
    val noSolutionFound: LiveData<Boolean> get() = _noSolutionFound

    var startX by mutableIntStateOf(-1)
    var startY by mutableIntStateOf(-1)
    var endX by mutableIntStateOf(-1)
    var endY by mutableIntStateOf(-1)

    init {
        viewModelScope.launch {
            val lastPath = withContext(Dispatchers.IO) { knightPathDao.getLastPath() }
            lastPath?.let {
                _boardSize.value = it.boardSize
                _maxMoves.value = it.maxMoves
                startX = it.startX
                startY = it.startY
                endX = it.endX
                endY = it.endY
                calculatePaths()
            }
        }
    }

    fun calculatePaths() {
        viewModelScope.launch(Dispatchers.Default) {
            val result = knightPathFinder(
                boardSize.value ?: 8,
                startX,
                startY,
                endX,
                endY,
                maxMoves.value ?: 3
            )
            withContext(Dispatchers.Main) {
                if (result.isEmpty()) {
                    _noSolutionFound.value = true
                } else {
                    _noSolutionFound.value = false
                    _paths.value = result
                }
            }
        }
    }

    fun updateBoardSize(newSize: Int) {
        _boardSize.value = newSize
    }

    fun updateMaxMoves(newMaxMoves: Int) {
        _maxMoves.value = newMaxMoves
    }

    fun onTileSelected(row: Int, col: Int) {
        if (startX == -1 && startY == -1) {
            startX = row
            startY = col
        } else if (endX == -1 && endY == -1) {
            endX = row
            endY = col
            calculatePaths()
        } else {
            startX = row
            startY = col
            endX = -1
            endY = -1
            _paths.value = emptyList()
        }
    }

    fun resetBoard() {
        _boardSize.value = 8
        _maxMoves.value = 3
        startX = -1
        startY = -1
        endX = -1
        endY = -1
        _paths.value = emptyList()
        _noSolutionFound.value = false
    }

    fun saveState() {
        viewModelScope.launch(Dispatchers.IO) {
            knightPathDao.savePath(
                KnightPath(
                    boardSize = boardSize.value ?: 8,
                    startX = startX,
                    startY = startY,
                    endX = endX,
                    endY = endY,
                    maxMoves = maxMoves.value ?: 3
                )
            )
        }
    }

    fun clearNoSolutionFound() {
        _noSolutionFound.value = false
    }
}
