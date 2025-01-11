package com.example.sudoku.ui

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.sudoku.ui.SettingsScreen
import com.example.sudoku.ui.theme.SudokuTheme
import kotlinx.serialization.Serializable

@Composable
fun SudokuApp(modifier: Modifier = Modifier) {

    val sudokuBoardScreenViewModel: SudokuBoardScreenViewModel = viewModel(factory = SudokuBoardScreenViewModel.Factory)
    val navController = rememberNavController()

    SudokuTheme {
        Scaffold(
            bottomBar = {
                BottomNavigationBar(
                    selectedItemIndex = sudokuBoardScreenViewModel.bottomNavigationSelectedItemIndex,
                    onNavigateItemClick = {index ->
                        sudokuBoardScreenViewModel.onNavBarItemClick(index)
                        when (index) {
                            0 -> navController.navigate(SolverScreen)
                            1 -> navController.navigate(HomeScreen)
                            2 -> navController.navigate(SettingsScreen)
                        }
                    },
                    modifier = Modifier
                )
            },
            modifier = modifier
                .background(color = MaterialTheme.colorScheme.surface)
                .fillMaxSize()
        ) { innerPadding ->
            @Composable
            fun getBackHandlerToSolverScreen() = BackHandler {
                navController.navigate(SolverScreen)
                sudokuBoardScreenViewModel.onNavBarItemClick(0)
            }

            NavHost(navController = navController, startDestination = SolverScreen) {
                composable<SolverScreen> {
                    getBackHandlerToSolverScreen()

                    Box(
                        modifier = Modifier
                            .padding(innerPadding)
                    ) {
                        val sudokuBoardUiState by sudokuBoardScreenViewModel.initSudokuBoardUiState.collectAsState()
                        when (sudokuBoardUiState) {
                            is InitSudokuBoardUiState.NotLoaded -> NewBoardScreen(onClickStart = { difficulty ->
                                sudokuBoardScreenViewModel.setSudokuBoardUiStateLoading()
                                sudokuBoardScreenViewModel.getSudokuBoard(difficulty)
                            })

                            is InitSudokuBoardUiState.Error -> LoadingError(onReloadClick = { sudokuBoardScreenViewModel.setSudokuBoardUiStateNotLoaded() })
                            is InitSudokuBoardUiState.Loading -> LoadingIcon()
                            is InitSudokuBoardUiState.Success -> {
                                sudokuBoardScreenViewModel.youWonAnimationComposableContext =
                                    rememberCoroutineScope().coroutineContext

                                val context = LocalContext.current

                                PuzzleSolverScreen(
                                    difficulty = sudokuBoardScreenViewModel.difficultyString,
                                    hintsLeft = sudokuBoardScreenViewModel.numHints.collectAsStateWithLifecycle().value,
                                    currentSudokuBoard = sudokuBoardScreenViewModel.currentSudokuBoard.collectAsStateWithLifecycle().value,
                                    sudokuBoardSolution = sudokuBoardScreenViewModel.sudokuBoardSolution,
                                    playerWon = sudokuBoardScreenViewModel.playerWon,
                                    playerWonAnimationProgress = sudokuBoardScreenViewModel.playerWonAnimationProgress.value,
                                    isNotesMode = sudokuBoardScreenViewModel.notesMode.collectAsStateWithLifecycle().value,
                                    numberCounts = sudokuBoardScreenViewModel.numberCounts.collectAsStateWithLifecycle().value,
                                    timeProvider = { sudokuBoardScreenViewModel.puzzleTimeString },
                                    selectedEntryProvider = { sudokuBoardScreenViewModel.selectedEntry },
                                    isPaused = sudokuBoardScreenViewModel.isPaused,
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
                                            sudokuBoardScreenViewModel.addOrRemoveNoteAtCurrentEntry(
                                                number
                                            )
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
                                    onClickUndo = { sudokuBoardScreenViewModel.undo() },
                                    onResumeClick = { sudokuBoardScreenViewModel.startTimer() }
                                )
                            }
                        }
                    }
                }
                composable<HomeScreen> {
                    getBackHandlerToSolverScreen()
                }
                composable<SettingsScreen> {
                    getBackHandlerToSolverScreen()
                }
            }
        }
    }
}


@Preview
@Composable
private fun SudokuAppPreview() {
}

@Serializable
object SolverScreen

@Serializable
object HomeScreen

@Serializable
object SettingsScreen