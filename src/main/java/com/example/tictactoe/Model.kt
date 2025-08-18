package com.example.tictactoe

import android.graphics.Paint
import android.graphics.RectF
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

class Model() {

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
    private var lastPlayer = BoardItem.AI
    private var gameOver = false
    private var AIPlayer = AI();

    init {
        this.board = arrayOf(
            arrayOf(BoardItem.Empty, BoardItem.Empty, BoardItem.Empty),
            arrayOf(BoardItem.Empty, BoardItem.Empty, BoardItem.Empty),
            arrayOf(BoardItem.Empty, BoardItem.Empty, BoardItem.Empty)
        )
    }

    fun reset() {
        this.board = arrayOf(
            arrayOf(BoardItem.Empty, BoardItem.Empty, BoardItem.Empty),
            arrayOf(BoardItem.Empty, BoardItem.Empty, BoardItem.Empty),
            arrayOf(BoardItem.Empty, BoardItem.Empty, BoardItem.Empty)
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

        val newBoard = board.map { it.copyOf() }.toTypedArray()
        newBoard[indexes.first][indexes.second] = BoardItem.Player;
        board = newBoard;

        val winner = AIPlayer.getWinner(board);
        if (AIPlayer.boardIsFull(board) || winner != null) {
            gameOver = true
        } else {
            lastPlayer = BoardItem.Player
            val aiMove: Pair<Int, Int> = AIPlayer.getMove(board);
            board[aiMove.first][aiMove.second] = BoardItem.AI;
            lastPlayer = BoardItem.AI

            if (AIPlayer.getWinner(board) != null) {
                gameOver = true
            }
        }
    }

    fun gameover(): Boolean {
        return gameOver;
    }

}