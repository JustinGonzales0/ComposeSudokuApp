package com.example.sudoku.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex

val sudokuBoard: MutableList<MutableList<Int>> = (0..8).map { i -> (1..9).map{ j -> j }} as MutableList<MutableList<Int>>

@Composable
fun SudokuBoard(
    sudokuBoard: MutableList<MutableList<Int>>,
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
            .padding(3.dp)
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
                ) {
                    Spacer(Modifier.fillMaxWidth(i / 3f))
                    VerticalDivider(
                        color = Color.DarkGray,
                        thickness = 2.dp,
                        modifier = Modifier
                            .width(0.dp)
                    )
                }
            }
            for (i in 1..8) {
                if (i == 3 || i == 6) {
                    continue
                }

                Row(
                    Modifier
                        .align(Alignment.TopStart)
                        .zIndex(-1f)
                ) {
                    Spacer(Modifier.fillMaxWidth(i / 9f))
                    VerticalDivider(
                        color = Color.Gray,
                        thickness = 1.dp,
                        modifier = Modifier
                            .width(0.dp)
                    )
                }
            }

            Column(
                modifier = Modifier
                    .zIndex(-1f)
            ) {
                sudokuBoard.forEachIndexed { index, row ->
                    SudokuRow(row = row)

                    if (index % 3 == 2 && index != 8) {
                        HorizontalDivider(
                            color = Color.DarkGray,
                            thickness = 2.dp,
                            modifier = Modifier
                                .height(0.dp)
                                .zIndex(-1f)
                        )
                    }
                    else if (index != 8) {
                        HorizontalDivider(
                            color = Color.Gray,
                            thickness = 1.dp,
                            modifier = Modifier
                                .height(0.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SudokuRow(
    row: MutableList<Int>,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        row.forEachIndexed { itemIndex, number ->
            Box(
                modifier = Modifier
                    .aspectRatio(1f)
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "$number",
                    color = Color.DarkGray
                )
            }
        }
    }
}

@Preview
@Composable
private fun SudokuRowPreview() {
    Surface() {
        SudokuRow(sudokuBoard[0])
    }
}