package com.assignment.chessboard.presentation

import android.app.Application
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.assignment.chessboard.presentation.theme.ChessboardAssignmentTheme
import com.assignment.chessboard.presentation.theme.PathColors
import com.assignment.chessboard.presentation.viewmodel.ChessboardViewModel

@Composable
fun Chessboard(viewModel: ChessboardViewModel) {
    val boardSize by viewModel.boardSize.observeAsState(8)
    val paths by viewModel.paths.observeAsState(emptyList())
    val startX = viewModel.startX
    val startY = viewModel.startY
    val endX = viewModel.endX
    val endY = viewModel.endY

    Box(
        modifier = Modifier
            .padding(top = 16.dp, bottom = 32.dp)
            .fillMaxWidth(1f)
            .fillMaxHeight(0.58f)
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectTapGestures { offset ->
                        val tileSize = size.width / boardSize
                        val col = (offset.x / tileSize).toInt()
                        val row = (offset.y / tileSize).toInt()
                        viewModel.onTileSelected(row, col)
                    }
                }
        ) {
            val tileSize = size.width / boardSize

            for (row in 0 until boardSize) {
                for (col in 0 until boardSize) {
                    drawRect(
                        color = if ((row + col) % 2 == 0) Color.LightGray else Color.DarkGray,
                        topLeft = androidx.compose.ui.geometry.Offset(
                            col * tileSize,
                            row * tileSize
                        ),
                        size = androidx.compose.ui.geometry.Size(tileSize, tileSize)
                    )
                }
            }

            if (startX != -1 && startY != -1) {
                drawRect(
                    color = Color.Green,
                    style = Stroke((tileSize*0.1).toFloat()),
                    topLeft = androidx.compose.ui.geometry.Offset(
                        startY * tileSize,
                        startX * tileSize
                    ),
                    size = androidx.compose.ui.geometry.Size(tileSize, tileSize)
                )
            }
            if (endX != -1 && endY != -1) {
                drawRect(
                    color = Color.Red,
                    style = Stroke((tileSize*0.1).toFloat()),
                    topLeft = androidx.compose.ui.geometry.Offset(
                        endY * tileSize,
                        endX * tileSize
                    ),
                    size = androidx.compose.ui.geometry.Size(tileSize, tileSize)
                )
            }

            paths.forEachIndexed { index, path ->
                val color = PathColors[index % PathColors.size]
                path.windowed(2).forEach { (start, end) ->
                    drawLine(
                        color = color,
                        start = androidx.compose.ui.geometry.Offset(
                            start.second * tileSize + tileSize / 2,
                            start.first * tileSize + tileSize / 2
                        ),
                        end = androidx.compose.ui.geometry.Offset(
                            end.second * tileSize + tileSize / 2,
                            end.first * tileSize + tileSize / 2
                        ),
                        strokeWidth = 12f,
                        pathEffect = PathEffect.cornerPathEffect(16f)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ChessboardPreview() {
    ChessboardAssignmentTheme {
        Chessboard(viewModel = ChessboardViewModel(application = Application()))
    }
}
