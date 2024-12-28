package com.example.sudoku.data.network

import kotlinx.serialization.Serializable

@Serializable
data class Newboard(
    val grids: List<Grid>,
    val message: String,
    val results: Int
)