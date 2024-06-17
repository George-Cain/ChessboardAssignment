package com.assignment.chessboard.presentation.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.assignment.chessboard.data.local.dao.ChessboardStateDao
import com.assignment.chessboard.data.local.database.AppDatabase
import com.assignment.chessboard.data.local.entity.ChessboardState
import com.assignment.chessboard.domain.utils.knightPathFinder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ChessboardViewModel(application: Application) : AndroidViewModel(application) {
    private val chessboardStateDao: ChessboardStateDao =
        AppDatabase.getDatabase(application).chessboardStateDao()

    private val _boardSize = MutableLiveData(8)
    val boardSize: LiveData<Int> get() = _boardSize

    private val _maxMoves = MutableLiveData(3)
    val maxMoves: LiveData<Int> get() = _maxMoves

    private val _paths = MutableLiveData<List<List<Pair<Int, Int>>>>(emptyList())
    val paths: LiveData<List<List<Pair<Int, Int>>>> get() = _paths

    private val _noSolutionFound = MutableLiveData(false)
    val noSolutionFound: LiveData<Boolean> get() = _noSolutionFound

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _isDarkTheme = MutableLiveData(false)
    val isDarkTheme: LiveData<Boolean> get() = _isDarkTheme

    var startX by mutableIntStateOf(-1)
    var startY by mutableIntStateOf(-1)
    var endX by mutableIntStateOf(-1)
    var endY by mutableIntStateOf(-1)

    init {
        viewModelScope.launch {
            val lastState = withContext(Dispatchers.IO) { chessboardStateDao.getLastState() }
            lastState?.let { state ->
                _boardSize.value = state.boardSize
                _maxMoves.value = state.maxMoves
                startX = state.startX
                startY = state.startY
                endX = state.endX
                endY = state.endY
                _isDarkTheme.value = state.isDarkTheme
                calculatePaths()
            }
        }
    }

    private fun calculatePaths() {
        viewModelScope.launch(Dispatchers.Default) {
            try {
                _isLoading.postValue(true)
                val result = knightPathFinder(
                    boardSize.value ?: 8,
                    startX,
                    startY,
                    endX,
                    endY,
                    maxMoves.value ?: 3
                )
                withContext(Dispatchers.Main) {
                    _noSolutionFound.value = result.isEmpty()
                    _paths.value = result
                    _isLoading.value = false
                    saveState()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _isLoading.value = false
                    _noSolutionFound.value = true
                }
            }
        }
    }

    fun updateBoardSize(newSize: Int) {
        updateState {
            _boardSize.value = newSize
        }
    }

    fun updateMaxMoves(newMaxMoves: Int) {
        updateState {
            _maxMoves.value = newMaxMoves
        }
    }

    fun updateTheme(isDark: Boolean) {
        updateState {
            _isDarkTheme.value = isDark
        }
    }

    private fun updateState(updateAction: () -> Unit) {
        updateAction()
        saveState()
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
        updateState {
            _boardSize.value = 8
            _maxMoves.value = 3
            startX = -1
            startY = -1
            endX = -1
            endY = -1
            _paths.value = emptyList()
            _noSolutionFound.value = false
        }
    }

    fun saveState() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                chessboardStateDao.saveState(
                    ChessboardState(
                        boardSize = boardSize.value ?: 8,
                        startX = startX,
                        startY = startY,
                        endX = endX,
                        endY = endY,
                        maxMoves = maxMoves.value ?: 3,
                        isDarkTheme = isDarkTheme.value ?: false
                    )
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun clearNoSolutionFound() {
        _noSolutionFound.value = false
    }

    override fun onCleared() {
        super.onCleared()
        saveState()
    }
}
