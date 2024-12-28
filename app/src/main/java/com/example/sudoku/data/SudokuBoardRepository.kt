package com.example.sudoku.data

import com.example.sudoku.data.network.PuzzleRequestBody
import com.example.sudoku.data.network.SudokuApiService
import com.example.sudoku.data.network.SudokuBoard
import com.example.sudoku.data.network.YouDoSudokuPuzzle
import kotlinx.serialization.json.JsonObject
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

interface SudokuBoardRepository {
    suspend fun getSudokuBoard(): YouDoSudokuPuzzle
}

class DefaultSudokuRepository(
    private val sudokuApiService: SudokuApiService
): SudokuBoardRepository {
    override suspend fun getSudokuBoard(): YouDoSudokuPuzzle {
        // val requestBodyObj: PuzzleRequestBody = PuzzleRequestBody("easy", true, true)

        val jsonRequestObj = JSONObject()
        jsonRequestObj.put("difficulty", "medium")
        jsonRequestObj.put("solution", true)
        jsonRequestObj.put("array", true)

        //val body: RequestBody = RequestBody.create(MediaType.parse("application/json"), requestBodyObj.toString())
        val requestBody: RequestBody = jsonRequestObj.toString().toRequestBody("application/json".toMediaTypeOrNull())

        return sudokuApiService.getYouDoPuzzle(
            requestBody
        )
    }
}