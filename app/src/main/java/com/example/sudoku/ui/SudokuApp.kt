package com.example.sudoku.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.sudoku.ui.theme.SudokuTheme
import androidx.lifecycle.viewmodel.compose.viewModel


@Composable
fun SudokuApp(modifier: Modifier = Modifier) {

    val sudokuBoardScreenViewModel: SudokuBoardScreenViewModel = viewModel(factory = SudokuBoardScreenViewModel.Factory)

    SudokuTheme {
        Scaffold(
            modifier = modifier
                .background(color = MaterialTheme.colorScheme.surface)
                .fillMaxSize()
        ) { innerPadding ->
            Column(
                verticalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                val sudokuBoardUiState by sudokuBoardScreenViewModel.initSudokuBoardUiState.collectAsState()
                when (sudokuBoardUiState) {
                    is InitSudokuBoardUiState.Error -> LoadingError( {

                        sudokuBoardScreenViewModel.apply {
                            setSudokuBoardUiStateLoading()
                            getSudokuBoard()
                        }
                    } )
                    is InitSudokuBoardUiState.Loading -> LoadingIcon()
                    is InitSudokuBoardUiState.Success -> {
                        InfoBar(
                            difficulty = sudokuBoardScreenViewModel.difficulty,
                            time = "1:23",
                            hintsLeft = 3,
                            modifier = Modifier
                        )
                        SudokuBoard(
                            currentSudokuBoard = sudokuBoardScreenViewModel.currentSudokuBoard.collectAsStateWithLifecycle().value,
                            sudokuBoardSolution = sudokuBoardScreenViewModel.sudokuBoardSolution,
                            selectedEntryProvider = { sudokuBoardScreenViewModel.selectedEntry },
                            onElementClick = { rowIndex, columnIndex ->
                                sudokuBoardScreenViewModel.setSelectedEntry(rowIndex, columnIndex)
                            },
                            onEntryKeyEvent = { code ->
                                sudokuBoardScreenViewModel.handleKeyInput(code)
                            }
                        )
                    }
                }

                NumberInputArea(
                    isNotesMode = sudokuBoardScreenViewModel.notesMode.collectAsStateWithLifecycle().value,
                    numberCounts = sudokuBoardScreenViewModel.numberCounts.collectAsStateWithLifecycle().value,
                    onInputButtonClick = { number ->
                        if (sudokuBoardScreenViewModel.notesMode.value) {
                            sudokuBoardScreenViewModel.addOrRemoveNote(number)
                        } else {
                            sudokuBoardScreenViewModel.setSudokuBoardCurrentState(number)
                        }
                    },
                    onClickErase = { sudokuBoardScreenViewModel.eraseEntry()},
                    onClickEdit = { sudokuBoardScreenViewModel.toggleNotesMode()},
                    modifier = Modifier
                        .weight(1f)
                )

            }
        }
    }
}


@Preview
@Composable
private fun SudokuAppPreview() {
}