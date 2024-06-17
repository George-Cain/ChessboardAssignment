package com.assignment.chessboard.presentation

import android.app.Application
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.assignment.chessboard.presentation.theme.ChessboardScreenTheme
import com.assignment.chessboard.presentation.viewmodel.ChessboardViewModel
import kotlinx.coroutines.launch

@Composable
fun ChessboardScreen(viewModel: ChessboardViewModel, modifier: Modifier = Modifier) {
    val boardSize by viewModel.boardSize.observeAsState(8)
    val maxMoves by viewModel.maxMoves.observeAsState(3)
    var sliderBoardSize by remember { mutableFloatStateOf(boardSize.toFloat()) }
    var sliderMaxMoves by remember { mutableFloatStateOf(maxMoves.toFloat()) }
    val noSolutionFound by viewModel.noSolutionFound.observeAsState(false)
    val isLoading by viewModel.isLoading.observeAsState(false)

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
            Text(text = "Board Size: $boardSize", fontSize = 21.sp)

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

            Text(text = "Max Moves: $maxMoves", fontSize = 21.sp)
            Slider(
                value = maxMoves.toFloat(),
                onValueChange = { newValue ->
                    sliderMaxMoves = newValue
                    viewModel.updateMaxMoves(newValue.toInt())
                },
                valueRange = 1f..5f,
                steps = 0
            )
            Spacer(modifier = Modifier.height(16.dp))

            Chessboard(viewModel)

            Button(
                modifier = Modifier
                    .fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                onClick = { viewModel.resetBoard() }) {
                Text("Reset", fontSize = 21.sp)
            }
        }

        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background.copy(alpha = 0.5f)),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    CircularProgressIndicator()
                    Spacer(modifier = Modifier.height(32.dp))
                    Text("Calculating paths...", fontSize = 21.sp)
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
    ChessboardScreenTheme {
        ChessboardScreen(viewModel = ChessboardViewModel(application = Application()))
    }
}
