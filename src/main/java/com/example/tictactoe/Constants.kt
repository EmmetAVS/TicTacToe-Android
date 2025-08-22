package com.example.tictactoe

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class Constants {

    object Numerical {
        val AI_TIME_DELAY = 500L
    }

    object Theme {
        val BACKGROUND = Color(59, 199, 235, 255);
        val BOARD_COLOR = Color(255, 255, 255, 255);
        val PLAYER_COLOR = Color(0, 0, 255, 255);
        val AI_COLOR = Color(255, 0, 0, 255);

        val BORDER_COLOR = Color(0, 0, 0);

        val STRIKE_THROUGH_COLOR = Color.Gray
    }

    object Style {

        val FONT_SIZE = 64.sp;
        val BORDER_WIDTH = 4.dp;

    }

}