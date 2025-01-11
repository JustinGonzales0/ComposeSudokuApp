package com.example.sudoku

import com.example.sudoku.data.NetworkSudokuRepository
import com.example.sudoku.data.SudokuBoardRepository
import com.example.sudoku.data.network.SudokuApiService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

interface AppContainer {
    val networkSudokuBoardRepository: SudokuBoardRepository
}

class DefaultAppContainer : AppContainer {
    private val baseUrl: String = "https://sudoku-api.vercel.app/api/"
    private val youDoSudokuBaseUrl: String = "https://you-do-sudoku-api.vercel.app/"

    @OptIn(ExperimentalSerializationApi::class)
    private val json = Json {
        explicitNulls = false
        ignoreUnknownKeys = true
        isLenient = true
    }

    private val retrofit = Retrofit.Builder()
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .baseUrl(youDoSudokuBaseUrl)
        .build()

    private val retrofitService: SudokuApiService by lazy {
        retrofit.create(SudokuApiService::class.java)
    }

    override val networkSudokuBoardRepository: SudokuBoardRepository by lazy {
        NetworkSudokuRepository(retrofitService)
    }
}