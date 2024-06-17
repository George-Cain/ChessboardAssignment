package com.assignment.chessboard.presentation

import android.app.Application
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.assignment.chessboard.presentation.theme.ChessboardAssignmentTheme
import com.assignment.chessboard.presentation.viewmodel.ChessboardViewModel

@Composable
fun ChessboardScreen(viewModel: ChessboardViewModel) {
    val boardSize by viewModel.boardSize.observeAsState(8)
    val maxMoves by viewModel.maxMoves.observeAsState(3)
    var sliderBoardSize by remember { mutableFloatStateOf(boardSize.toFloat()) }
    var sliderMaxMoves by remember { mutableFloatStateOf(maxMoves.toFloat()) }
    val paths by viewModel.paths.observeAsState(emptyList())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Board Size: $boardSize")
        Slider(
            value = boardSize.toFloat(),
            onValueChange = { newValue ->
                sliderBoardSize = newValue
                viewModel.updateBoardSize(newValue.toInt())
            },
            valueRange = 6f..16f,
            steps = 0
        )
        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Max Moves: $maxMoves")
        Slider(
            value = maxMoves.toFloat(),
            onValueChange = { newValue ->
                sliderMaxMoves = newValue
                viewModel.updateMaxMoves(newValue.toInt())
            },
            valueRange = 1f..10f,
            steps = 0
        )
        Spacer(modifier = Modifier.height(16.dp))

        Chessboard(viewModel)

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(onClick = { viewModel.resetBoard() }) {
                Text("Reset")
            }
            Button(onClick = { viewModel.undoMove() }) {
                Text("Undo")
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun ChessboardScreenPreview() {
    ChessboardAssignmentTheme {
        ChessboardScreen(viewModel = ChessboardViewModel(application = Application()))
    }
}
