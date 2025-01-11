package com.example.sudoku.ui

import android.R.attr.enabled
import android.R.attr.onClick
import android.R.attr.text
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun NewBoardScreen(
    onClickStart: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly,
        modifier = modifier
            .fillMaxSize()
            .padding(10.dp)
    ) {

        var newGameDifficulty by remember {
            mutableIntStateOf(0)
        }

        Text(
            text = "New Game",
            fontSize = 30.sp
        )

        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier
                .height(100.dp)
        ) {
            Text(
                text = "Difficulty:",
                color = Color.DarkGray,
                fontSize = 18.sp
            )

            Row (
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.width(240.dp)
            ) {
                @Composable
                fun getButtonColors(difficulty: Int): ButtonColors {
                    return ButtonColors(
                        containerColor = if (newGameDifficulty == difficulty) MaterialTheme.colorScheme.primary else Color.LightGray,
                        contentColor = MaterialTheme.colorScheme.onSecondary,
                        disabledContentColor = Color.Black,
                        disabledContainerColor = Color.LightGray
                    )
                }

                TextButton(
                    colors = getButtonColors(0),
                    onClick = { newGameDifficulty = 0 },
                    modifier = Modifier
                        .height(34.dp)
                        .width(60.dp)
                ) {
                    Text(
                        text = "Easy",
                        fontSize = 14.sp
                    )
                }

                TextButton(
                    colors = getButtonColors(1),
                    onClick = { newGameDifficulty = 1 },
                    modifier = Modifier
                        .height(34.dp)
                        .width(80.dp)
                ) {
                    Text(
                        text = "Medium",
                        fontSize = 14.sp
                    )
                }

                TextButton(
                    colors = getButtonColors(2),
                    onClick = { newGameDifficulty = 2 },
                    modifier = Modifier
                        .height(34.dp)
                        .width(60.dp)
                ) {
                    Text(
                        text = "Hard",
                        fontSize = 14.sp
                    )
                }
            }
        }

        Text(
            text = "Hints: ${
                when (newGameDifficulty) {
                    0 -> "3"
                    1 -> "2"
                    2 -> "1"
                    else -> ""
                }
            }",
            color = Color.DarkGray,
            fontSize = 18.sp,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
        )

        TextButton(
            onClick = { onClickStart(newGameDifficulty) },
            colors = ButtonColors(
                containerColor = MaterialTheme.colorScheme.secondary,
                contentColor = MaterialTheme.colorScheme.onSecondary,
                disabledContentColor = Color.Black,
                disabledContainerColor = Color.LightGray
            ),
            modifier = Modifier
                .height(40.dp)
                .width(100.dp)
        ) {
            Text(
                text = "Start",
                fontSize = 23.sp
            )
        }
    }
}

@Preview(widthDp = 300, heightDp = 600)
@Composable
private fun NewBoardScreenPreview() {
    Surface {
        NewBoardScreen(onClickStart = {})
    }
}