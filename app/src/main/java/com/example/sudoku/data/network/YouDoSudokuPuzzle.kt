package com.example.sudoku.data.network

import kotlinx.serialization.Serializable

@Serializable
data class YouDoSudokuPuzzle(
    val difficulty: String,
    val puzzle: List<List<String>>,
    val solution: List<List<String>>
)