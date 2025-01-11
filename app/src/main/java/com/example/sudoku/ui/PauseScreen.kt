package com.example.sudoku.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sudoku.R

@Composable
fun PauseScreen(
    currentTime: String,
    onResumeClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly,
        modifier = modifier
            .padding(5.dp)
            .fillMaxSize()
    ) {
        Column (
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .height(230.dp)
        ) {
            Icon(
                painter = painterResource(R.drawable.baseline_pause_24),
                contentDescription = "Pause icon",
                modifier = Modifier
                    .size(50.dp)
            )

            Text(
                text = stringResource(R.string.game_is_currently_paused),
                fontSize = 20.sp
            )

            Text(
                text = "Current time: $currentTime",
                color = Color.DarkGray,
                fontSize = 20.sp,
                modifier = Modifier
                    .padding(bottom = 20.dp)
            )

            TextButton(
                onClick = onResumeClick,
                colors = ButtonColors(
                    containerColor = MaterialTheme.colorScheme.secondary,
                    contentColor = MaterialTheme.colorScheme.onSecondary,
                    disabledContentColor = Color.Black,
                    disabledContainerColor = Color.LightGray
                ),
                modifier = Modifier
                    .height(50.dp)
                    .width(100.dp)
            ) {
                Text(
                    text = "Resume",
                    fontSize = 18.sp
                )
            }
        }

    }
}

@Preview
@Composable
private fun PauseScreenPreview() {
    Surface {
        PauseScreen(
            currentTime = "1:32",
            onResumeClick = {}
        )
    }
}