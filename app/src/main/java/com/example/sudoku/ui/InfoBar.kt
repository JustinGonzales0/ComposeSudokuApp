package com.example.sudoku.ui

import android.R.attr.data
import android.R.attr.tag
import android.R.attr.text
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun InfoBar(
    difficulty: String,
    timeProvider: () -> String,
    hintsLeft: Int,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .padding(start = 10.dp, end = 10.dp)
            .fillMaxWidth()
    ) {
        InfoBarDataDisplay(
            tag = "Difficulty",
            dataProvider = { difficulty })
        InfoBarDataDisplay(
            tag = "Time",
            dataProvider = timeProvider)
        InfoBarDataDisplay(
            tag = "Hints left",
            dataProvider = { hintsLeft.toString() })
    }
}

@Composable
fun InfoBarDataDisplay(
    tag: String,
    dataProvider: () -> String,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Text(text = tag)
        Text(
            text = dataProvider(),
            color = Color.DarkGray
        )
    }
}

@Preview
@Composable
private fun InfoBarPreview() {
    Surface {
        InfoBar(
            difficulty = "Medium",
            timeProvider = { "1:23" },
            hintsLeft = 3,
            modifier = Modifier
        )
    }
}