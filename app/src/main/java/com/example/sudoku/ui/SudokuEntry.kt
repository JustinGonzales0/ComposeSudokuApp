package com.example.sudoku.ui

import android.R.attr.text
import android.util.Log.i
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.Key.Companion.K
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import java.util.Map.entry

@Composable
fun SudokuEntry(
    entry: SudokuBoardEntryState,
    rowIndex: Int,
    columnIndex: Int,
    solution: Int,
    selectedEntryProvider: () -> Pair<Int, Int>,
    selectedNumberProvider: () -> Int,
    onElementClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isSelected: Boolean by remember {
        derivedStateOf {
            selectedEntryProvider().first == rowIndex && selectedEntryProvider().second == columnIndex
        }
    }
    val isErrorEntry = entry.number != 0 && entry.number != solution

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .zIndex(-2f)
            .aspectRatio(1f)
            .fillMaxSize()
            .then(
                if (isErrorEntry) {
                    Modifier.background(MaterialTheme.colorScheme.error)
                } else if (isSelected) {
                    Modifier.background(Color.Gray)
                } else if (rowIndex == selectedEntryProvider().first || columnIndex == selectedEntryProvider().second) {
                    Modifier.background(MaterialTheme.colorScheme.surfaceVariant)
                } else {
                    Modifier
                }
            )
            .clickable(onClick = onElementClick)
    ) {
        if (entry.number == 0 && entry.isMutable) {
            NotesGrid(
                notesList = entry.notesList,
                isSelected = isSelected,
                selectedNumberProvider = selectedNumberProvider
            )
        } else if (entry.number != 0) {
            Text(
                text = entry.number.toString(),
                color =
                if (isErrorEntry) {
                    Color.White
                } else if (entry.number == selectedNumberProvider() && selectedEntryProvider() != Pair<Int, Int>(rowIndex, columnIndex)) {
                    Color.Magenta
                } else if (entry.isMutable) {
                    if (isSelected) {
                        Color.Cyan
                    } else {
                        Color.Blue
                    }
                } else {
                    if (isSelected) {
                        Color.White
                    } else {
                        Color.DarkGray
                    }
                }
            )
        }
    }
}

@Composable
fun NotesGrid(
    notesList: Set<Int>,
    isSelected: Boolean,
    selectedNumberProvider: () -> Int,
    modifier: Modifier = Modifier
) {
    LazyVerticalGrid (
        columns = GridCells.Fixed(3),
        modifier = modifier
            .aspectRatio(1f)
            .padding()
    ) {
        items(
            items = (1..9).toList()
        ) { number ->
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .aspectRatio(1f)
                    .fillMaxHeight()
            ) {
                if (notesList.contains(number)) {
                    Text(
                        fontSize = 7.sp,
                        textAlign = TextAlign.Center,
                        style = LocalTextStyle.current.merge(
                            TextStyle(
                                lineHeight = 2.5.em,
                                platformStyle = PlatformTextStyle(
                                    includeFontPadding = false
                                ),
                                lineHeightStyle = LineHeightStyle(
                                    alignment = LineHeightStyle.Alignment.Center,
                                    trim = LineHeightStyle.Trim.None
                                )
                            )
                        ),
                        color = if (selectedNumberProvider() == number) {
                            Color.Magenta
                        } else if (isSelected) {
                            Color.White
                        } else {
                            Color.DarkGray
                        },
                        modifier = Modifier
                            .wrapContentHeight(align = Alignment.CenterVertically)
                            .align(Alignment.Center),
                        text = number.toString()
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun NotesGridPreview() {
    Surface(
        color = Color.DarkGray
    ) {
        NotesGrid(
            notesList = setOf(1, 2, 3, 4, 5, 6, 7, 8, 9),
            isSelected = true,
            selectedNumberProvider = { 1 },
            modifier = Modifier
                .size(50.dp)
        )
    }
}