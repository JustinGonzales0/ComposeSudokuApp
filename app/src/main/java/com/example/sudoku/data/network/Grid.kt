package com.example.sudoku.data.network

import kotlinx.serialization.Serializable

@Serializable
data class Grid(
    val difficulty: String,
    val solution: List<List<Int>>,
    val value: List<List<Int>>
)