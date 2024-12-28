package com.example.sudoku.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex

val sudokuBoard: List<List<Int>> =
    (0..8).map { i -> (1..9).map { j -> if (j % 2 == 0) { j } else { 0 } }}

@Composable
fun SudokuBoard(
    currentSudokuBoard: SnapshotStateList<SnapshotStateList<SudokuBoardEntryState>>,
    sudokuBoardSolution: List<List<Int>>,
    selectedEntryProvider: () -> Pair<Int, Int>,
    onElementClick: ((Int, Int) -> Unit)?,
    onEntryKeyEvent: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .padding(5.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(color = Color.LightGray)
            .border(
                BorderStroke(color = Color.Black, width = 4.dp),
                shape = RoundedCornerShape(10.dp)
            )
            .padding(4.dp)
            .focusable()
            .onKeyEvent {
                if (it.type == KeyEventType.KeyDown) {
                    onEntryKeyEvent(it.key.keyCode)
                    true
                } else {
                    false
                }
            }
    ) {
        Box(
            modifier = Modifier
                .aspectRatio(1f)
                .zIndex(1f)
        ) {
            for (i in 1..2) {
                Row(
                    Modifier
                        .align(Alignment.TopStart)
                        .fillMaxWidth()
                        .zIndex(2f)
                ) {
                    Spacer(Modifier.fillMaxWidth(i / 3f))
                    VerticalDivider(
                        color = Color.DarkGray,
                        thickness = 3.dp,
                        modifier = Modifier
                            .width(0.dp)
                            .absoluteOffset(x = (-1.5).dp)
                            .zIndex(2f)
                    )
                }

                Column(
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .fillMaxHeight()
                        .zIndex(2f)
                ) {
                    Spacer(Modifier.fillMaxHeight(i / 3f))

                    HorizontalDivider(
                        color = Color.DarkGray,
                        thickness = 3.dp,
                        modifier = Modifier
                            .height(1.5.dp)
                            .absoluteOffset(y = (-1.5).dp)
                            .zIndex(2f)
                    )
                }
            }
            for (i in 1..8) {
                if (i == 3 || i == 6) {
                    continue
                }
                Row(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .fillMaxWidth()
                        .zIndex(1f)
                ) {
                    Spacer(Modifier.fillMaxWidth(i / 9f))
                    VerticalDivider(
                        color = Color.Gray,
                        thickness = 2.5.dp,
                        modifier = Modifier
                            .width(0.dp)
                            .absoluteOffset(x = (-1).dp)
                            .zIndex(1f)
                    )
                }

                Column(
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .fillMaxHeight()
                        .zIndex(1f)
                ) {
                    Spacer(Modifier.fillMaxHeight(i / 9f))
                    HorizontalDivider(
                        color = Color.Gray,
                        thickness = 2.5.dp,
                        modifier = Modifier
                            .height(1.dp)
                            .absoluteOffset(y = (-1).dp)
                            .zIndex(1f)
                    )
                }
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier
                    .fillMaxHeight()
                    .zIndex(-1f)
            ) {
                currentSudokuBoard.forEachIndexed { rowIndex, row ->
                    SudokuRow(
                        row = row,
                        rowSolution = sudokuBoardSolution[rowIndex],
                        rowIndex = rowIndex,
                        selectedEntryProvider = selectedEntryProvider,
                        selectedNumberProvider = {
                            if (selectedEntryProvider().first > -1 && selectedEntryProvider().first < 9 && selectedEntryProvider().second > -1 && selectedEntryProvider().second < 9) {
                                currentSudokuBoard[selectedEntryProvider().first][selectedEntryProvider().second].number
                            } else {
                                -1
                            }
                        },
                        onElementClick = onElementClick!!
                    )
                }
            }
        }
    }
}

@SuppressLint("UnrememberedMutableState")
@Preview
@Composable
private fun SudokuBoardPreview() {
}