package com.example.sudoku.ui

import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex

@Composable
fun SudokuRow(
    row: SnapshotStateList<SudokuBoardEntryState>,
    rowSolution: List<Int>,
    rowIndex: Int,
    selectedEntryProvider: () -> Pair<Int, Int>,
    selectedNumberProvider: () -> Int,
    onElementClick: ((Int, Int) -> Unit)?,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .zIndex(-1f)
            .fillMaxWidth()
            .padding(0.dp)
    ) {
        row.forEachIndexed { columnIndex, entry ->
            SudokuEntry(
                entry = entry,
                rowIndex = rowIndex,
                columnIndex = columnIndex,
                solution = rowSolution[columnIndex],
                selectedEntryProvider = selectedEntryProvider,
                selectedNumberProvider = selectedNumberProvider,
                onElementClick = { onElementClick!!(rowIndex, columnIndex) },
                modifier.weight(1f)
            )
        }
    }
}

@Preview
@Composable
private fun SudokuRowPreview() {
    Surface {
//        SudokuRow(
//            sudokuBoard[0].map { mutableStateOf<SudokuBoardEntryState>( SudokuBoardEntryState(number = it, isMutable = false)) },
//            0,
//            Pair(0, 0),
//            null
//        )
    }
}