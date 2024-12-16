package com.example.sudoku.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.sudoku.ui.theme.SudokuTheme


@Composable
fun SudokuApp(modifier: Modifier = Modifier) {
    SudokuTheme {
        Scaffold(
            modifier = modifier
                .background(color = MaterialTheme.colorScheme.surface)
                .fillMaxSize()
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                SudokuBoard(sudokuBoard, Modifier.padding(innerPadding))
                Column (
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f),
                    verticalArrangement = Arrangement.Center
                ) {
                    NumberInputRow()
                }
            }
        }
    }
}

@Preview
@Composable
private fun SudokuBoardPreview() {
    SudokuApp()
}