package com.example.sudoku.ui

import android.widget.Toast
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
import androidx.compose.ui.platform.LocalContext
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
                            timeProvider = { sudokuBoardScreenViewModel.puzzleTimeString },
                            hintsLeft = sudokuBoardScreenViewModel.numHints.collectAsStateWithLifecycle().value,
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

                val context = LocalContext.current

                NumberInputArea(
                    isNotesMode = sudokuBoardScreenViewModel.notesMode.collectAsStateWithLifecycle().value,
                    numberCounts = sudokuBoardScreenViewModel.numberCounts.collectAsStateWithLifecycle().value,
                    onInputButtonClick = { number ->
                        if (sudokuBoardScreenViewModel.notesMode.value) {
                            sudokuBoardScreenViewModel.addOrRemoveNoteAtCurrentEntry(number)
                        } else {
                            sudokuBoardScreenViewModel.setCurrentSudokuBoardEntry(sudokuBoardScreenViewModel.getCurrentSudokuBoardEntry().copy(number = number))
                        }
                    },
                    onClickErase = { sudokuBoardScreenViewModel.eraseEntry() },
                    onClickEdit = { sudokuBoardScreenViewModel.toggleNotesMode() },
                    onClickHint = {
                        // Show toast based on result of getting a hint
                        // we do this here because getting the context in the viewmodel is difficult
                        val hintResult = sudokuBoardScreenViewModel.getHint()
                        if (hintResult == -1) {
                            Toast.makeText(context, "You have no more hints!", Toast.LENGTH_SHORT).show()
                        } else if (hintResult == 0) {
                            Toast.makeText(context, "You have no open spots for a hint!", Toast.LENGTH_SHORT).show()
                        }
                    },
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