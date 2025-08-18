    package com.example.tictactoe

    import android.R.attr.onClick
    import android.util.Log
    import android.widget.Button
    import androidx.compose.foundation.BorderStroke
    import androidx.compose.foundation.background
    import androidx.compose.foundation.clickable
    import androidx.compose.foundation.layout.*
    import androidx.compose.material3.*
    import androidx.compose.runtime.*
    import androidx.compose.ui.Alignment
    import androidx.compose.ui.Modifier
    import androidx.compose.ui.graphics.Brush
    import androidx.compose.ui.graphics.Color
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
            board.forEachIndexed { y, row ->
                Row {
                    row.forEachIndexed { x, cell ->
                        Box(
                            modifier = Modifier
                                .size(100.dp)
                                .padding(4.dp)
                                .background(Constants.Theme.BOARD_COLOR)
                                .clickable(
                                    board[y][x] == Model.BoardItem.Empty
                                            && !model.gameover()
                                ) {
                                    model.update(Pair<Int, Int>(y, x))
                                },
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
        }
    }