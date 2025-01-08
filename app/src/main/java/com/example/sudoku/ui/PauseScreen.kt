package com.example.sudoku.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.sudoku.R

@Composable
fun PauseScreen(
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
                .height(200.dp)
        ) {
            Icon(
                painter = painterResource(R.drawable.baseline_pause_24),
                contentDescription = "Pause icon",
                modifier = Modifier
                    .size(50.dp)
            )

            Text(stringResource(R.string.game_is_currently_paused))

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
                Text("Resume")
            }
        }

    }
}

@Preview
@Composable
private fun PauseScreenPreview() {
    Surface {
        PauseScreen(onResumeClick = {})
    }
}