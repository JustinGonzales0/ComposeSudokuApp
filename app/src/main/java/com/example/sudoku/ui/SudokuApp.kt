package com.example.sudoku.ui

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.sudoku.ui.theme.SudokuTheme
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.coroutineScope


@Composable
fun SudokuApp(modifier: Modifier = Modifier) {

    val sudokuBoardScreenViewModel: SudokuBoardScreenViewModel = viewModel(factory = SudokuBoardScreenViewModel.Factory)

    SudokuTheme {
        Scaffold(
            modifier = modifier
                .background(color = MaterialTheme.colorScheme.surface)
                .fillMaxSize()
        ) { innerPadding ->
            Box (
                modifier = Modifier
                    .padding(innerPadding)
            ) {
                val sudokuBoardUiState by sudokuBoardScreenViewModel.initSudokuBoardUiState.collectAsState()
                when (sudokuBoardUiState) {
                    is InitSudokuBoardUiState.Error -> LoadingError({

                        sudokuBoardScreenViewModel.apply {
                            setSudokuBoardUiStateLoading()
                            getSudokuBoard()
                        }
                    })

                    is InitSudokuBoardUiState.Loading -> LoadingIcon()
                    is InitSudokuBoardUiState.Success -> {
                        sudokuBoardScreenViewModel.youWonAnimationComposableContext =
                            rememberCoroutineScope().coroutineContext

                        val context = LocalContext.current

                        PuzzleSolverScreen(
                            difficulty = sudokuBoardScreenViewModel.difficulty,
                            hintsLeft = sudokuBoardScreenViewModel.numHints.collectAsStateWithLifecycle().value,
                            currentSudokuBoard = sudokuBoardScreenViewModel.currentSudokuBoard.collectAsStateWithLifecycle().value,
                            sudokuBoardSolution = sudokuBoardScreenViewModel.sudokuBoardSolution,
                            playerWon = sudokuBoardScreenViewModel.playerWon,
                            playerWonAnimationProgress = sudokuBoardScreenViewModel.playerWonAnimationProgress.value,
                            isNotesMode = sudokuBoardScreenViewModel.notesMode.collectAsStateWithLifecycle().value,
                            numberCounts = sudokuBoardScreenViewModel.numberCounts.collectAsStateWithLifecycle().value,
                            timeProvider = { sudokuBoardScreenViewModel.puzzleTimeString },
                            selectedEntryProvider = { sudokuBoardScreenViewModel.selectedEntry },
                            onPauseClick = { sudokuBoardScreenViewModel.pauseTimer() },
                            onElementClick = { rowIndex, columnIndex ->
                                sudokuBoardScreenViewModel.setSelectedEntry(
                                    rowIndex,
                                    columnIndex
                                )
                            },
                            onEntryKeyEvent = { code ->
                                sudokuBoardScreenViewModel.handleKeyInput(code)
                            },
                            onInputButtonClick = { number: Int ->
                                if (sudokuBoardScreenViewModel.notesMode.value) {
                                    sudokuBoardScreenViewModel.addOrRemoveNoteAtCurrentEntry(number)
                                } else {
                                    sudokuBoardScreenViewModel.setCurrentSudokuBoardEntry(
                                        sudokuBoardScreenViewModel.getCurrentSudokuBoardEntry()
                                            .copy(number = number)
                                    )
                                }
                            },
                            onClickErase = { sudokuBoardScreenViewModel.eraseEntry() },
                            onClickEdit = { sudokuBoardScreenViewModel.toggleNotesMode() },
                            onClickHint = {
                                // Show toast based on result of getting a hint
                                // we do this here because getting the context in the viewmodel is difficult
                                val hintResult = sudokuBoardScreenViewModel.getHint()
                                if (hintResult == -1) {
                                    Toast.makeText(
                                        context,
                                        "You have no more hints!",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                } else if (hintResult == 0) {
                                    Toast.makeText(
                                        context,
                                        "You have no open spots for a hint!",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            },
                            onClickUndo = { sudokuBoardScreenViewModel.undo() }
                        )
                    }
                }
            }
        }
    }
}


@Preview
@Composable
private fun SudokuAppPreview() {
}