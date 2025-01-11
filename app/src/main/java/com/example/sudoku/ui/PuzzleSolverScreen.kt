package com.example.sudoku.ui

import android.telecom.VideoProfile.isPaused
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.unit.dp

@Composable
fun PuzzleSolverScreen(
    difficulty: String,
    hintsLeft: Int,
    currentSudokuBoard: SnapshotStateList<SnapshotStateList<SudokuBoardEntryState>>,
    sudokuBoardSolution: List<List<Int>>,
    playerWon: Boolean,
    playerWonAnimationProgress: Float,
    isNotesMode: Boolean,
    numberCounts: List<Int>,
    isPaused: Boolean,
    timeProvider: () -> String,
    selectedEntryProvider: () -> Pair<Int, Int>,
    onPauseClick: () -> Unit,
    onElementClick: ((Int, Int) -> Unit)?,
    onEntryKeyEvent: (Long) -> Unit,
    onInputButtonClick: (Int) -> Unit,
    onClickErase: () -> Unit,
    onClickEdit: () -> Unit,
    onClickHint: () -> Unit,
    onClickUndo: () -> Unit,
    onResumeClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        contentAlignment = Alignment.TopCenter,
        modifier = modifier
    ) {
        Column(
            verticalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier
                .fillMaxHeight(.8f)
                .then (
                    if (isPaused) {
                        Modifier.blur(10.dp)
                    } else {
                        Modifier
                    }
                )
        ) {
            InfoBar(
                difficulty = difficulty,
                timeProvider = timeProvider,
                hintsLeft = hintsLeft,
                onPauseClick = onPauseClick,
                modifier = Modifier
            )

            SudokuBoard(
                currentSudokuBoard = currentSudokuBoard,
                sudokuBoardSolution = sudokuBoardSolution,
                selectedEntryProvider = selectedEntryProvider,
                playerWon = playerWon,
                playerWonAnimationProgress = playerWonAnimationProgress,
                onElementClick = onElementClick,
                onEntryKeyEvent = onEntryKeyEvent
            )

            NumberInputArea(
                isNotesMode = isNotesMode,
                numberCounts = numberCounts,
                onInputButtonClick = onInputButtonClick,
                onClickErase = onClickErase,
                onClickEdit = onClickEdit,
                onClickHint = onClickHint,
                onClickUndo = onClickUndo,
                modifier = Modifier
            )
        }

        if (isPaused) {
            PauseScreen(
                currentTime = timeProvider(),
                onResumeClick = onResumeClick
            )
        }
    }
}