package com.example.sudoku.data.network

import kotlinx.serialization.Serializable

@Serializable
data class SudokuBoard(
    val newboard: Newboard
)