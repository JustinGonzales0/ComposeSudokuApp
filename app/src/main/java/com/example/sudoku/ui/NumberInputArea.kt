package com.example.sudoku.ui

import android.R.attr.contentDescription
import android.R.attr.onClick
import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.sudoku.R

@Composable
fun NumberInputArea(
    isNotesMode: Boolean,
    numberCounts: List<Int>,
    onInputButtonClick: (number: Int) -> Unit,
    onClickErase: () -> Unit,
    onClickEdit: () -> Unit,
    onClickHint: () -> Unit,
    onClickUndo: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column (
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, end = 20.dp, bottom = 10.dp)
        ) {
            NotesButton(onClickEdit = onClickEdit, isNotesMode = isNotesMode)
            HintButton(onClick = onClickHint)
            UndoButton(onClick = onClickUndo)
            EraseButton(onClick = onClickErase)
        }
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = modifier
                .padding(5.dp)
        ) {
            for (i in 1..9) {
                TextButton(
                    onClick = { onInputButtonClick(i) },
                    colors = ButtonColors(
                        containerColor = Color.Gray,
                        contentColor = Color.Black,
                        disabledContainerColor = Color.LightGray,
                        disabledContentColor = Color.Black
                    ),
                    shape = RoundedCornerShape(10.dp),
                    enabled = numberCounts[i - 1] != 9,
                    modifier = Modifier
                        .weight(1f)
                        .aspectRatio(1f)
                        .padding(2.dp)
                ) {
                    Text(
                        text = "$i",
                        color = Color.White
                    )

                }
            }
        }
    }
}

@Preview
@Composable
private fun NumberInputAreaPreview() {
    Surface() {
        NumberInputArea(onClickErase = {},
            onInputButtonClick = {},
            onClickEdit = {},
            isNotesMode = false,
            numberCounts = listOf(0),
            onClickHint = {},
            onClickUndo = {}
        )
    }
}

@Composable
fun NotesButton(
    onClickEdit: () -> Unit,
    isNotesMode: Boolean,
    modifier: Modifier = Modifier
) {
    IconButton(
        onClick = onClickEdit,
        modifier = modifier
    ) {
        Icon(
            imageVector = if (!isNotesMode) Icons.Outlined.Edit else Icons.Filled.Edit,
            contentDescription = stringResource(R.string.edit_button),
            modifier = Modifier.size(80.dp)
        )
    }
}

@Composable
fun EraseButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    IconButton(
        onClick = onClick,
        modifier = modifier
    ) {
        Icon(
            painter = painterResource(R.drawable.outline_ink_eraser_24),
            contentDescription = "Clear button",
            modifier = Modifier.size(80.dp)
        )
    }
}

@Composable
fun HintButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    IconButton(
        onClick = onClick,
        modifier = modifier
    ) {
        Icon(
            painter = painterResource(R.drawable.baseline_lightbulb_outline_24),
            contentDescription = "Hint button",
            modifier = Modifier.size(80.dp)
        )
    }
}

@Composable
fun UndoButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    IconButton(
        onClick = onClick,
        modifier = modifier,
    ) {
        Icon(
            imageVector = Icons.Filled.Refresh,
            contentDescription = "Undo button",
            modifier = Modifier.size(80.dp)
        )
    }
}