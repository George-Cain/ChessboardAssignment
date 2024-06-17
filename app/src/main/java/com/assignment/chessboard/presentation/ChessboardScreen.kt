package com.assignment.chessboard.presentation

import android.app.Application
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.assignment.chessboard.presentation.theme.ChessboardAssignmentTheme
import com.assignment.chessboard.presentation.viewmodel.ChessboardViewModel
import kotlinx.coroutines.launch
import androidx.compose.runtime.livedata.observeAsState

@Composable
fun ChessboardScreen(viewModel: ChessboardViewModel) {
    val boardSize by viewModel.boardSize.observeAsState(8)
    val maxMoves by viewModel.maxMoves.observeAsState(3)
    var sliderBoardSize by remember { mutableFloatStateOf(boardSize.toFloat()) }
    var sliderMaxMoves by remember { mutableFloatStateOf(maxMoves.toFloat()) }
    val paths by viewModel.paths.observeAsState(emptyList())
    val noSolutionFound by viewModel.noSolutionFound.observeAsState(false)

    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(noSolutionFound) {
        if (noSolutionFound) {
            coroutineScope.launch {
                snackbarHostState.showSnackbar(
                    message = "No solution has been found",
                    duration = SnackbarDuration.Short
                )
                viewModel.clearNoSolutionFound()
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
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
            }
        }

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ChessboardScreenPreview() {
    ChessboardAssignmentTheme {
        ChessboardScreen(viewModel = ChessboardViewModel(application = Application()))
    }
}
