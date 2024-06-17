package com.assignment.chessboard.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NightsStay
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import com.assignment.chessboard.presentation.theme.ChessboardScreenTheme
import com.assignment.chessboard.presentation.viewmodel.ChessboardViewModel

class MainActivity : ComponentActivity() {
    private val viewModel: ChessboardViewModel by viewModels()

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val isDarkTheme by viewModel.isDarkTheme.observeAsState(false)
            ChessboardScreenTheme(darkTheme = isDarkTheme) {
                Surface(color = MaterialTheme.colorScheme.background) {
                    Scaffold(
                        topBar = {
                            TopAppBar(
                                title = {},
                                actions = {
                                    val icon =
                                        if (isDarkTheme) Icons.Filled.WbSunny else Icons.Filled.NightsStay
                                    IconButton(onClick = { viewModel.updateTheme(!isDarkTheme) }) {
                                        Icon(imageVector = icon, contentDescription = null)
                                    }
                                }
                            )
                        }
                    ) { padding ->
                        ChessboardScreen(viewModel, Modifier.padding(padding))
                    }
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.saveState()
    }
}
