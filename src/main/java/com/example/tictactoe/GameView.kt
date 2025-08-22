package com.example.tictactoe

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp


@Composable
fun TicTacToeBoard(model: Model) {

    val board = model.board

    Log.d("", "Redrawing")

    Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Constants.Theme.BACKGROUND),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 25.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Box(
                    modifier = Modifier,
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = Model.BoardItem.Player.getMarker(),
                        color = Model.BoardItem.Player.getColor(),
                        fontSize = Constants.Style.FONT_SIZE,
                        textAlign = TextAlign.Center,
                    )
                }

                Box (
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = model.getScoreText(),
                        color = Constants.Theme.BOARD_COLOR,
                        fontSize = Constants.Style.FONT_SIZE / 1.25,
                        textAlign = TextAlign.Center
                    )
                }

                Box(
                    modifier = Modifier,
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = Model.BoardItem.AI.getMarker(),
                        color = Model.BoardItem.AI.getColor(),
                        fontSize = Constants.Style.FONT_SIZE,
                        textAlign = TextAlign.Center
                    )
                }

            }
            board.forEachIndexed { y, row ->
                Row {
                    row.forEachIndexed { x, cell ->
                        Box(
                            modifier = Modifier
                                .size(100.dp)
                                .background(Constants.Theme.BACKGROUND)
                                .clickable(
                                    board[y][x] == Model.BoardItem.Empty
                                            && !model.gameover()
                                ) {
                                    model.update(Pair<Int, Int>(y, x))
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Box (
                                modifier = Modifier
                                    .size(96.dp)
                                    .background(Constants.Theme.BOARD_COLOR),
                                contentAlignment = Alignment.Center
                            ) {
                                if (cell.getMarker().isNotEmpty()) {
                                    Text(
                                        text = cell.getMarker(),
                                        color = cell.getColor(),
                                        fontSize = Constants.Style.FONT_SIZE,
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }

                            val winningLineDirection = model.getWinningLineDirection(Pair<Int, Int>(y, x))

                            if (winningLineDirection != Model.WinningLineDirection.NONE) {
                                Canvas(modifier = Modifier.fillMaxSize()) {
                                    val strokeWidth = 8.dp.toPx()
                                    val color = Constants.Theme.STRIKE_THROUGH_COLOR

                                    when (winningLineDirection) {
                                        Model.WinningLineDirection.HORIZONTAL -> {
                                            drawLine(
                                                color = color,
                                                start = Offset(0f, size.height / 2),
                                                end = Offset(size.width, size.height / 2),
                                                strokeWidth = strokeWidth,
                                                cap = StrokeCap.Round
                                            )
                                        }
                                        Model.WinningLineDirection.VERTICAL -> {
                                            drawLine(
                                                color = color,
                                                start = Offset(size.width / 2, 0f),
                                                end = Offset(size.width / 2, size.height),
                                                strokeWidth = strokeWidth,
                                                cap = StrokeCap.Round
                                            )
                                        }
                                        Model.WinningLineDirection.DIAGONAL_TOP_LEFT_TO_BOTTOM_RIGHT -> {
                                            drawLine(
                                                color = color,
                                                start = Offset(0f, 0f),
                                                end = Offset(size.width, size.height),
                                                strokeWidth = strokeWidth,
                                                cap = StrokeCap.Round
                                            )
                                        }
                                        Model.WinningLineDirection.DIAGONAL_BOTTOM_LEFT_TO_TOP_RIGHT -> {
                                            drawLine(
                                                color = color,
                                                start = Offset(0f, size.height),
                                                end = Offset(size.width, 0f),
                                                strokeWidth = strokeWidth,
                                                cap = StrokeCap.Round
                                            )
                                        }
                                        Model. WinningLineDirection.NONE -> {}
                                    }
                                }
                            }
                        }
                    }
                }
            }

            var expanded by remember { mutableStateOf(false) }
            var selectedDifficulty by remember { mutableStateOf(AI.Difficulty.Medium) }

            Box(
                modifier = Modifier
                    .wrapContentSize(Alignment.TopStart)
                    .padding(top = 15.dp)
            ) {
                Button(
                    onClick = { expanded = true },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Constants.Theme.BOARD_COLOR,
                        contentColor = Constants.Theme.BACKGROUND
                    ),
                    border = BorderStroke(
                        width = 1.dp,
                        color = Color.Gray
                    )
                ) {
                    Text("Difficulty: ${selectedDifficulty.name}")
                }
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    AI.Difficulty.entries.forEach { difficultyValue ->
                        Log.d("AI Difficulty", difficultyValue.toString())
                        DropdownMenuItem(
                            text = {
                                Text(difficultyValue.toString())
                            },
                            onClick = {
                                model.setDifficulty(difficultyValue)
                                selectedDifficulty = difficultyValue
                                expanded = false
                            }
                        )
                    }
                }
            }

            Button(
                onClick = {
                    model.reset();
                },
                modifier = Modifier
                    .padding(top = 15.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Constants.Theme.BOARD_COLOR,
                    contentColor = Constants.Theme.BACKGROUND
                ),
                border = BorderStroke(
                    width = 1.dp,
                    color = Color.Gray
                )
            ) {
                Text(
                    "Reset",
                )
            }

            var gameOverText = "";
            if (model.gameover()) {
                gameOverText = "Game Over!"
            }

            Text(
                gameOverText,
                modifier = Modifier
                    .padding(top = 15.dp),
                fontSize = Constants.Style.FONT_SIZE / 2,
                color = Constants.Theme.BOARD_COLOR
            )

        }
    }