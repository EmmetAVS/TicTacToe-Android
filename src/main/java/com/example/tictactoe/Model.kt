package com.example.tictactoe

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import kotlinx.coroutines.delay
import java.util.Timer
import kotlin.concurrent.schedule

class Model() {

    public enum class WinningLineDirection {
        HORIZONTAL,
        VERTICAL,
        DIAGONAL_TOP_LEFT_TO_BOTTOM_RIGHT,
        DIAGONAL_BOTTOM_LEFT_TO_TOP_RIGHT,
        NONE
    }

    public enum class BoardItem {
        Player {
            override fun getMarker(): String {
                return "X"
            }

            override fun getColor(): Color {
                return Constants.Theme.PLAYER_COLOR;
            }

        },
        AI {
            override fun getMarker(): String {
                return "O"
            }

            override fun getColor(): Color {
                return Constants.Theme.AI_COLOR;
            }

        },
        Empty {
            override fun getMarker(): String {
                return ""
            }

            override fun getColor(): Color {
                return Constants.Theme.BACKGROUND;
            }
        };
        abstract fun getMarker(): String
        abstract fun getColor(): Color
    }

    var board: Array<Array<BoardItem>> by mutableStateOf(
        arrayOf(
            arrayOf(BoardItem.Empty, BoardItem.Empty, BoardItem.Empty),
            arrayOf(BoardItem.Empty, BoardItem.Empty, BoardItem.Empty),
            arrayOf(BoardItem.Empty, BoardItem.Empty, BoardItem.Empty)
        )
    )

    var winningLineDirections: Array<Array<WinningLineDirection>> = arrayOf(
        arrayOf(WinningLineDirection.NONE, WinningLineDirection.NONE, WinningLineDirection.NONE),
        arrayOf(WinningLineDirection.NONE, WinningLineDirection.NONE, WinningLineDirection.NONE),
        arrayOf(WinningLineDirection.NONE, WinningLineDirection.NONE, WinningLineDirection.NONE)
    )
    private var lastPlayer = BoardItem.AI
    private var gameOver = false
    private var AIPlayer = AI(AI.Difficulty.Medium);

    private var aiWins = 0;
    private var playerWins = 0;
    private var draws = 0;

    init {
        this.board = arrayOf(
            arrayOf(BoardItem.Empty, BoardItem.Empty, BoardItem.Empty),
            arrayOf(BoardItem.Empty, BoardItem.Empty, BoardItem.Empty),
            arrayOf(BoardItem.Empty, BoardItem.Empty, BoardItem.Empty)
        )
    }

    fun getWinningLineDirection(indexes: Pair<Int, Int>): WinningLineDirection {

        return winningLineDirections[indexes.first][indexes.second]

    }

    fun getScoreText(): String {

        return "$playerWins : $aiWins"

    }

    fun setDifficulty(difficulty: AI.Difficulty) {
        AIPlayer = AI(difficulty);
    }

    fun reset() {
        this.board = arrayOf(
            arrayOf(BoardItem.Empty, BoardItem.Empty, BoardItem.Empty),
            arrayOf(BoardItem.Empty, BoardItem.Empty, BoardItem.Empty),
            arrayOf(BoardItem.Empty, BoardItem.Empty, BoardItem.Empty)
        )
        this.winningLineDirections = arrayOf(
            arrayOf(WinningLineDirection.NONE, WinningLineDirection.NONE, WinningLineDirection.NONE),
            arrayOf(WinningLineDirection.NONE, WinningLineDirection.NONE, WinningLineDirection.NONE),
            arrayOf(WinningLineDirection.NONE, WinningLineDirection.NONE, WinningLineDirection.NONE)
        )
        lastPlayer = BoardItem.AI;
        gameOver = false;
    }

    fun update(indexes: Pair<Int, Int>) {

        Log.d("", "Indexes: $indexes")

        if (lastPlayer == BoardItem.Player || gameOver) {
            return;
        } else if (board[indexes.first][indexes.second] != BoardItem.Empty) {
            return;
        }

        board[indexes.first][indexes.second] = BoardItem.Player;
        resetBoard()

        var winner = AIPlayer.getWinner(board, winningLineDirections);

        if (AIPlayer.boardIsFull(board) || winner != null) {

            gameOver = true
        } else {
            lastPlayer = BoardItem.Player


            Timer().schedule(Constants.Numerical.AI_TIME_DELAY) {
                handleAIMove()
            }
        }

        if (winner == BoardItem.AI) {
            aiWins ++;
        } else if (winner == BoardItem.Player) {
            playerWins ++;
        } else {
            if (AIPlayer.boardIsFull(board)) {
                draws ++;
            }
        }
    }

    fun gameover(): Boolean {
        return gameOver;
    }

    fun lastPlayer(): BoardItem {
        return lastPlayer;
    }

    private fun resetBoard() {

        val newBoard = board.map { it.copyOf() }.toTypedArray()
        board = newBoard;

    }

    private fun handleAIMove() {

        val aiMove: Pair<Int, Int> = AIPlayer.getMove(board);
        board[aiMove.first][aiMove.second] = BoardItem.AI;
        lastPlayer = BoardItem.AI

        resetBoard()

        val winner = AIPlayer.getWinner(board, winningLineDirections)
        if (winner != null) {
            gameOver = true
        }

        if (winner == BoardItem.AI) {
            aiWins ++;
        } else if (winner == BoardItem.Player) {
            playerWins ++;
        } else {
            if (AIPlayer.boardIsFull(board)) {
                draws ++;
            }
        }
    }

}