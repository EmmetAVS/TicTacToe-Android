package com.example.tictactoe;

class AI {

    fun boardIsFull(board: Array<Array<Model.BoardItem>>): Boolean {

        for (r in 0..2) {
            for (c in 0..2) {
                if (board[r][c] == Model.BoardItem.Empty) return false;
            }
        }

        return true;

    }

    fun getWinner(board: Array<Array<Model.BoardItem>>): Model.BoardItem? {

        for (r in 0..2) {

            if (board[r][0] == board[r][1] && board[r][1] == board[r][2] && board[r][0] != Model.BoardItem.Empty) {
                return board[r][0];
            }

        }

        for (c in 0..2) {

            if (board[0][c] == board[1][c] && board[1][c] == board[2][c] && board[0][c] != Model.BoardItem.Empty) {
                return board[0][c];
            }

        }

        if (board[0][0] == board[1][1] && board[1][1] == board[2][2] && board[0][0] != Model.BoardItem.Empty) {
            return board[0][0]
        } else if (board[0][2] == board[1][1] && board[1][1] == board[2][0] && board[1][1] != Model.BoardItem.Empty) {
            return board[0][2];
        }

        return null;

    }

    fun findBestMove(
        board: Array<Array<Model.BoardItem>>,
        player: Model.BoardItem,
        depth: Int,
        lastMove: Pair<Int, Int>
    ): Triple<Pair<Int, Int>, Int, Int> {

        val winner = getWinner(board)
        if (winner != null) {
            when (winner) {
                Model.BoardItem.AI -> return Triple(lastMove, 10 - depth, depth)
                Model.BoardItem.Player -> return Triple(lastMove, depth - 10, depth)
                Model.BoardItem.Empty -> Triple(lastMove, 0, depth)
            }
        }

        if (boardIsFull(board)) {
            return Triple(lastMove, 0, depth)
        }

        var bestMove: Triple<Pair<Int, Int>, Int, Int>? = null

        val moveList = mutableListOf<Pair<Int, Int>>()
        for (r in 0..2) {
            for (c in 0..2) {
                moveList.add(Pair(r, c))
            }
        }
        moveList.shuffle()

        for (item in moveList) {
            val r = item.first
            val c = item.second
            if (board[r][c] == Model.BoardItem.Empty) {

                board[r][c] = player

                val nextPlayer = if (player == Model.BoardItem.AI) Model.BoardItem.Player else Model.BoardItem.AI
                val result = findBestMove(board, nextPlayer, depth + 1, Pair(r, c))

                if (player == Model.BoardItem.AI) {
                    if (bestMove == null || result.second > bestMove.second ||
                        (result.second == bestMove.second && result.third < bestMove.third)) {
                        bestMove = Triple(Pair(r, c), result.second, result.third)
                    }
                } else {
                    if (bestMove == null || result.second < bestMove.second ||
                        (result.second == bestMove.second && result.third < bestMove.third)) {
                        bestMove = Triple(Pair(r, c), result.second, result.third)
                    }
                }
                board[r][c] = Model.BoardItem.Empty
            }
        }

        return bestMove!!
    }

    fun getMove(board: Array<Array<Model.BoardItem>>): Pair<Int, Int> {

        val lastMove: Pair<Int, Int> = Pair(0, 0);
        return findBestMove(board, Model.BoardItem.AI, 0, lastMove).first;

    }

}
