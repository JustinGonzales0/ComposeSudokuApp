package com.example.sudoku.ui

import android.net.http.HttpException
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.Key.Companion.Eight
import androidx.compose.ui.input.key.Key.Companion.Five
import androidx.compose.ui.input.key.Key.Companion.Four
import androidx.compose.ui.input.key.Key.Companion.Nine
import androidx.compose.ui.input.key.Key.Companion.NumPad1
import androidx.compose.ui.input.key.Key.Companion.NumPad2
import androidx.compose.ui.input.key.Key.Companion.NumPad3
import androidx.compose.ui.input.key.Key.Companion.NumPad4
import androidx.compose.ui.input.key.Key.Companion.NumPad5
import androidx.compose.ui.input.key.Key.Companion.NumPad6
import androidx.compose.ui.input.key.Key.Companion.NumPad7
import androidx.compose.ui.input.key.Key.Companion.NumPad8
import androidx.compose.ui.input.key.Key.Companion.NumPad9
import androidx.compose.ui.input.key.Key.Companion.One
import androidx.compose.ui.input.key.Key.Companion.Seven
import androidx.compose.ui.input.key.Key.Companion.Six
import androidx.compose.ui.input.key.Key.Companion.Three
import androidx.compose.ui.input.key.Key.Companion.Two
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.sudoku.SudokuApplication
import com.example.sudoku.data.SudokuBoardRepository
import com.example.sudoku.data.network.YouDoSudokuPuzzle
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okio.IOException
import java.util.Stack
import kotlin.Int
import kotlin.collections.mutableListOf
import kotlin.random.Random

sealed interface InitSudokuBoardUiState {
    data class Success(
        val initialYouDoSudokuPuzzle: YouDoSudokuPuzzle
    ): InitSudokuBoardUiState
    data object Error: InitSudokuBoardUiState
    data object Loading: InitSudokuBoardUiState
}

data class SudokuBoardEntryState(
    val number: Int,
    val isMutable: Boolean,
    val notesList: Set<Int>
) {
}

data class UserAction(
    val indices: Pair<Int, Int>,
    val newEntryState: SudokuBoardEntryState,
    val originalEntryState: SudokuBoardEntryState
) {

}

class SudokuBoardScreenViewModel (
    private val sudokuBoardRepository: SudokuBoardRepository,
) : ViewModel() {

    // States
    private val _initSudokuBoardUiState: MutableStateFlow<InitSudokuBoardUiState> = MutableStateFlow(
        InitSudokuBoardUiState.Loading)
    val initSudokuBoardUiState: StateFlow<InitSudokuBoardUiState> = _initSudokuBoardUiState.asStateFlow()

    var selectedEntry by mutableStateOf(Pair<Int, Int>(-1, -1))
        private set

    private val _currentSudokuBoard: MutableStateFlow<SnapshotStateList<SnapshotStateList<SudokuBoardEntryState>>> =
        MutableStateFlow(mutableStateListOf<SnapshotStateList<SudokuBoardEntryState>>().apply {
            repeat(9) {
                add(mutableStateListOf<SudokuBoardEntryState>().apply {
                    repeat(9) { add(SudokuBoardEntryState(number = 0, isMutable = true, notesList = setOf())) }
                })
            }
        })

    val currentSudokuBoard: StateFlow<SnapshotStateList<SnapshotStateList<SudokuBoardEntryState>>> =
        _currentSudokuBoard.asStateFlow()

    var sudokuBoardSolution: List<List<Int>> = (0..8).map {
        (0..8).map { number ->
            number
        }
    }
        private set

    var difficulty: String = ""
        private set

    private val _notesMode: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val notesMode: StateFlow<Boolean> = _notesMode.asStateFlow()

    val _numberCounts: MutableStateFlow<List<Int>> = MutableStateFlow(mutableListOf<Int>().apply {
        repeat(9) {
            add(0)
        }
    })
    val numberCounts: StateFlow<List<Int>> = _numberCounts.asStateFlow()

    var quickKeyboardMove: Boolean = false

    private var _puzzleTime: Int = 0

    var puzzleTimeString by mutableStateOf("0:00")

    private var timerJob: Job? = null

    private val _numHints = MutableStateFlow(3)
    val numHints = _numHints.asStateFlow()

    private var userActions = Stack<UserAction>()

    // Initialization
    init {
        getSudokuBoard()
    }

    fun getSudokuBoard() {
        viewModelScope.launch {
            _initSudokuBoardUiState.value = try {
                InitSudokuBoardUiState.Success(
                    sudokuBoardRepository.getSudokuBoard()
                )

            } catch (e: IOException) {
                InitSudokuBoardUiState.Error
            } catch (e: HttpException) {
                InitSudokuBoardUiState.Error
            }

            if (_initSudokuBoardUiState.value is InitSudokuBoardUiState.Success) {
                val puzzle = (_initSudokuBoardUiState.value as InitSudokuBoardUiState.Success).initialYouDoSudokuPuzzle

                puzzle.puzzle.forEachIndexed { rowIndex, row ->
                    row.forEachIndexed { columnIndex, item ->
                        _currentSudokuBoard.value[rowIndex][columnIndex] = SudokuBoardEntryState(
                            number = item.toInt(), isMutable = item == "0", notesList = setOf()
                        )
                    }
                }

                sudokuBoardSolution =
                    puzzle.solution.map { row ->
                        row.map { entry ->
                            entry.toInt()
                        }
                    }

                difficulty = puzzle.difficulty

                // Capitalize beginning of word for difficulty
                difficulty = difficulty.replaceFirstChar { it.uppercaseChar() }

                setNumberCounts()

                startTimer()

                userActions = Stack<UserAction>()
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as SudokuApplication)
                val sudokuRepository = application.container.sudokuBoardRepository
                SudokuBoardScreenViewModel(sudokuBoardRepository = sudokuRepository)
            }
        }
    }

    // Entries
    fun setSelectedEntry(rowIndex: Int, columnIndex: Int) {
        selectedEntry = Pair(rowIndex, columnIndex)
    }

    fun setSudokuBoardUiStateLoading() {
        _initSudokuBoardUiState.value = InitSudokuBoardUiState.Loading
    }

    fun setCurrentSudokuBoardEntry(entryState: SudokuBoardEntryState) {
        setSudokuBoardEntry(Pair(selectedEntry.first, selectedEntry.second), entryState)
    }

    fun setSudokuBoardEntry(indices: Pair<Int, Int>, entryState: SudokuBoardEntryState) {
        if (!getCurrentSudokuBoardEntry().isMutable || currentEntryIsOutOfBounds()) {
            return
        }

        val originalEntryState = _currentSudokuBoard.value[indices.first][indices.second]

        _currentSudokuBoard.value[indices.first][indices.second] = entryState

        if (entryState.number != 0) {
            _currentSudokuBoard.value[indices.first][indices.second] = entryState.copy(notesList = setOf())
        }

        setNumberCounts()

        if (indices == selectedEntry && !notesMode.value && sudokuBoardSolution[selectedEntry.first][selectedEntry.second] == entryState.number) {
            adaptNotes(entryState.number)
        }

        userActions.push(UserAction(indices, originalEntryState, entryState))
    }

    fun getCurrentSudokuBoardEntry(): SudokuBoardEntryState {
        return getSudokuBoardEntry(selectedEntry)
    }

    fun getSudokuBoardEntry(indices: Pair<Int, Int>): SudokuBoardEntryState {
        if (entryIsOutOfBounds(indices)) {
            return SudokuBoardEntryState(-1, false, setOf())
        }

        return _currentSudokuBoard.value[indices.first][indices.second]
    }

    fun eraseEntry() {
        setCurrentSudokuBoardEntry(getCurrentSudokuBoardEntry().copy(number = 0, notesList = setOf()))
    }

    fun currentEntryIsOutOfBounds(): Boolean =
        entryIsOutOfBounds(selectedEntry)

    fun entryIsOutOfBounds(indices: Pair<Int, Int>): Boolean =
        indices.first < 0 || indices.second < 0 || indices.first > 8 || indices.second > 8

    // Notes
    fun toggleNotesMode() {
        _notesMode.value = !_notesMode.value
    }

    fun addOrRemoveNoteAtCurrentEntry(number: Int) {
        addOrRemoveNote(selectedEntry, number)
    }

    fun addOrRemoveNote(indices: Pair<Int, Int>, number: Int) {
        if (getSudokuBoardEntry(indices).number != 0) {
            return
        }

        // Log.d("JSG", "in addOrRemoveNote")

        if (getSudokuBoardEntry(indices).notesList.contains(number)) {
            setSudokuBoardEntry(indices, getSudokuBoardEntry(indices).copy(
                notesList = getSudokuBoardEntry(indices).notesList - number
            ))
        } else {
            setSudokuBoardEntry(indices, getSudokuBoardEntry(indices).copy(
                    notesList = getSudokuBoardEntry(indices).notesList + number
                )
            )
        }
    }

    fun findCelRange(entry: Pair<Int, Int>): Pair<Pair<Int, Int>, Pair<Int, Int>> {
        fun findTriRange(spot: Int) : Pair<Int, Int> {
            if (spot >= 0 && spot <= 2) {
                return Pair(0, 2)
            } else if (spot >= 3 && spot <= 5) {
                return Pair(3, 5)
            } else if (spot >= 6 && spot <= 8) {
                return Pair(6, 8)
            } else {
                return Pair(-1, -1)
            }
        }

        return Pair(findTriRange(entry.first), findTriRange(entry.second))
    }

    fun isInCelRange(entry: Pair<Int, Int>, celRange: Pair<Pair<Int, Int>, Pair<Int, Int>>) : Boolean {
        return (entry.first >= celRange.first.first && entry.first <= celRange.first.second) &&
                entry.second >= celRange.second.first && entry.second <= celRange.second.second
    }

    private fun adaptNotes(inputtedNumber: Int) {

        // Log.d("JSG", "in adaptNotes")

        val currentEntryBlockRange: Pair<Pair<Int, Int>, Pair<Int, Int>> = findCelRange(selectedEntry)

        fun shouldRemoveNote(entry: Pair<Int, Int>): Boolean {
            // Log.d("JSG", "in shouldRemoveNote, at $entry")
            return (entry.first == selectedEntry.first || entry.second == selectedEntry.second) ||
                    isInCelRange(entry, currentEntryBlockRange)
        }

        currentSudokuBoard.value.forEachIndexed { rowIndex, row ->
            row.forEachIndexed { columnIndex, entry ->
                if (entry.isMutable && shouldRemoveNote(Pair(rowIndex, columnIndex)) &&
                    entry.notesList.contains(inputtedNumber)) {
                    addOrRemoveNote(Pair(rowIndex, columnIndex), inputtedNumber)
                }
            }
        }
    }

    fun setNumberCounts() {

        var newCounts = mutableListOf<Int>().apply {
            repeat(9) {
                add(0)
            }
        }

        currentSudokuBoard.value.forEach { i ->
            i.forEach { j ->
                if (j.number in 1..9) {
                    newCounts[j.number - 1] += 1
                }
            }
        }

        _numberCounts.value = newCounts

        // Debug log:
//        Log.d("JSG", "-- new counts --")
//        numberCounts.value.forEachIndexed { index, num ->
//            val trueIndex: Int = index + 1
//            Log.d("JSG", "numberCounts: $trueIndex- $num")
//        }
    }

    // Hints
    fun getHint(): Int {
        if (numHints.value < 1) {
            return -1
        }

        val openings: MutableList<Pair<Int, Int>> = mutableListOf()

        currentSudokuBoard.value.forEachIndexed { rowIndex, row ->
            row.forEachIndexed { columnIndex, entry ->
                if (entry.isMutable && entry.number == 0) {
                    openings.add(Pair(rowIndex, columnIndex))
                }
            }
        }

        if (openings.isEmpty()) {
            return 0
        }

        val selectedHintEntry: Pair<Int, Int> = openings[Random.nextInt(openings.size)]
        val currentHintSudokuBoardEntry = _currentSudokuBoard.value[selectedHintEntry.first][selectedHintEntry.second]
        _currentSudokuBoard.value[selectedHintEntry.first][selectedHintEntry.second] = currentHintSudokuBoardEntry.copy(
            number = sudokuBoardSolution[selectedHintEntry.first][selectedHintEntry.second],
            notesList = setOf(),
        )

        _numHints.value--

        return openings.size
    }

    // Keyboard
    private fun toggleQuickKeyboardMove() {
        quickKeyboardMove = !quickKeyboardMove
    }

    fun handleKeyInput(code: Long) {
        if (code in One.keyCode .. Nine.keyCode || code in NumPad1.keyCode .. NumPad9.keyCode) {

            var newNumber = 0

            newNumber = when (code) {
                One.keyCode -> 1
                Two.keyCode -> 2
                Three.keyCode -> 3
                Four.keyCode -> 4
                Five.keyCode -> 5
                Six.keyCode -> 6
                Seven.keyCode -> 7
                Eight.keyCode -> 8
                Nine.keyCode -> 9
                NumPad1.keyCode -> 1
                NumPad2.keyCode -> 2
                NumPad3.keyCode -> 3
                NumPad4.keyCode -> 4
                NumPad5.keyCode -> 5
                NumPad6.keyCode -> 6
                NumPad7.keyCode -> 7
                NumPad8.keyCode -> 8
                NumPad9.keyCode -> 9
                else -> 0
            }

            if (numberCounts.value[newNumber - 1] == 9) {
                return
            }

            if (!_notesMode.value) {
                setCurrentSudokuBoardEntry(getCurrentSudokuBoardEntry().copy(number = newNumber))
            } else {
                addOrRemoveNoteAtCurrentEntry(newNumber)
            }

            return
        }

        if (code == Key.DirectionUp.keyCode || code == Key.W.keyCode) {
            if (quickKeyboardMove) {
                selectedEntry = Pair<Int, Int>(0, selectedEntry.second)
                return
            }

            if (selectedEntry.first - 1 > -1) {
                selectedEntry = Pair<Int, Int>(selectedEntry.first - 1, selectedEntry.second)
                return
            }
        }

        if (code == Key.DirectionDown.keyCode || code == Key.S.keyCode) {
            if (quickKeyboardMove) {
                selectedEntry = Pair<Int, Int>(8, selectedEntry.second)
                return
            }

            if (selectedEntry.first + 1 < 9) {
                selectedEntry = Pair<Int, Int>(selectedEntry.first + 1, selectedEntry.second)

                return
            }
        }

        if (code == Key.DirectionLeft.keyCode || code == Key.A.keyCode) {
            if (quickKeyboardMove) {
                selectedEntry = Pair<Int, Int>(selectedEntry.first, 0)
            }

            if (selectedEntry.second - 1 > -1) {
                selectedEntry = Pair<Int, Int>(selectedEntry.first, selectedEntry.second - 1)
                return
            }

        }

        if (code == Key.DirectionRight.keyCode || code == Key.D.keyCode) {
            if (quickKeyboardMove) {
                selectedEntry = Pair<Int, Int>(selectedEntry.first, 8)
                return
            }

            if (selectedEntry.second + 1 < 9) {
                selectedEntry = Pair<Int, Int>(selectedEntry.first, selectedEntry.second + 1)
                return
            }

        }

        if (code == Key.Backspace.keyCode || code == Key.E.keyCode) {
            eraseEntry()

            return
        }

        if (code == Key.F.keyCode) {
            toggleNotesMode()

            return
        }

        if (code == Key.Q.keyCode) {
            toggleQuickKeyboardMove()

            return
        }
    }

    // Timer
    fun startTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while(true) {
                delay(1000)
                _puzzleTime++
                puzzleTimeString = getTimerString(_puzzleTime)
            }
        }
    }

    fun pauseTimer() {
        timerJob?.cancel()
    }

    fun stopTimer() {
        _puzzleTime = 0
        timerJob?.cancel()
    }

    fun getTimerString(seconds: Int) : String {
        val minutes: Int = (seconds / 60).toInt()
        val seconds: Int = (seconds % 60)

        var secondsString: String = seconds.toString()
        if (seconds < 10) {
            secondsString = "0$secondsString"
        }

        val minutesString: String = minutes.toString()

        return "$minutesString:$secondsString"
    }
}