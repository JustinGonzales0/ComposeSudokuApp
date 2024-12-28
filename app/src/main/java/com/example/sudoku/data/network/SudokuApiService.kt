package com.example.sudoku.data.network

import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface SudokuApiService {
    @GET("dosoku")
    suspend fun getNewBoard(): SudokuBoard

    @POST("api")
    suspend fun getYouDoPuzzle(
        @Body requestBody: RequestBody
    ): YouDoSudokuPuzzle
}