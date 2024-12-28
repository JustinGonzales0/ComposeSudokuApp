package com.example.sudoku.data.network

import kotlinx.serialization.Serializable

@Serializable
data class PuzzleRequestBody(
    val difficulty: String,
    val solution: Boolean,
    val array: Boolean
)