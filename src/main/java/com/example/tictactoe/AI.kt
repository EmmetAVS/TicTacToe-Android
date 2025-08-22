package com.example.tictactoe;

class AI(val difficulty: Difficulty) {

    public enum class Difficulty {
        Easy,
        Medium,
        Hard
    }

    fun boardIsFull(board: Array<Array<Model.BoardItem>>): Boolean {

        for (r in 0..2) {
            for (c in 0..2) {
                if (board[r][c] == Model.BoardItem.Empty) return false;
            }
        }

        return true;

    }

    fun getWinner(board: Array<Array<Model.BoardItem>>, winningLineDirections: Array<Array<Model.WinningLineDirection>>? = null): Model.BoardItem? {

        for (r in 0..2) {

            if (board[r][0] == board[r][1] && board[r][1] == board[r][2] && board[r][0] != Model.BoardItem.Empty) {
                winningLineDirections?.get(r)[0] = Model.WinningLineDirection.HORIZONTAL
                winningLineDirections?.get(r)[1] = Model.WinningLineDirection.HORIZONTAL
                winningLineDirections?.get(r)[2] = Model.WinningLineDirection.HORIZONTAL
                return board[r][0];
            }

        }

        for (c in 0..2) {

            if (board[0][c] == board[1][c] && board[1][c] == board[2][c] && board[0][c] != Model.BoardItem.Empty) {
                winningLineDirections?.get(0)[c] = Model.WinningLineDirection.VERTICAL
                winningLineDirections?.get(1)[c] = Model.WinningLineDirection.VERTICAL
                winningLineDirections?.get(2)[c] = Model.WinningLineDirection.VERTICAL
                return board[0][c];
            }

        }

        if (board[0][0] == board[1][1] && board[1][1] == board[2][2] && board[0][0] != Model.BoardItem.Empty) {
            winningLineDirections?.get(0)[0] = Model.WinningLineDirection.DIAGONAL_TOP_LEFT_TO_BOTTOM_RIGHT
            winningLineDirections?.get(1)[1] = Model.WinningLineDirection.DIAGONAL_TOP_LEFT_TO_BOTTOM_RIGHT
            winningLineDirections?.get(2)[2] = Model.WinningLineDirection.DIAGONAL_TOP_LEFT_TO_BOTTOM_RIGHT
            return board[0][0]
        } else if (board[0][2] == board[1][1] && board[1][1] == board[2][0] && board[1][1] != Model.BoardItem.Empty) {
            winningLineDirections?.get(0)[2] = Model.WinningLineDirection.DIAGONAL_BOTTOM_LEFT_TO_TOP_RIGHT
            winningLineDirections?.get(1)[1] = Model.WinningLineDirection.DIAGONAL_BOTTOM_LEFT_TO_TOP_RIGHT
            winningLineDirections?.get(2)[0] = Model.WinningLineDirection.DIAGONAL_BOTTOM_LEFT_TO_TOP_RIGHT
            return board[0][2];
        }

        return null;

    }

    private fun getMoveList(board: Array<Array<Model.BoardItem>>): MutableList<Pair<Int, Int>> {
        val moveList = mutableListOf<Pair<Int, Int>>()
        for (r in 0..2) {
            for (c in 0..2) {
                if (board[r][c] == Model.BoardItem.Empty)
                    moveList.add(Pair(r, c))
            }
        }
        moveList.shuffle()

        return moveList
    }

    private fun findBestMoveHard(
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

        val moveList = getMoveList(board)

        for (item in moveList) {
            val r = item.first
            val c = item.second
            if (board[r][c] == Model.BoardItem.Empty) {

                board[r][c] = player

                val nextPlayer = if (player == Model.BoardItem.AI) Model.BoardItem.Player else Model.BoardItem.AI
                val result = findBestMoveHard(board, nextPlayer, depth + 1, Pair(r, c))

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

    private fun findBestMoveMedium(board: Array<Array<Model.BoardItem>>): Pair<Int, Int> {

        val moveList = getMoveList(board)

        for (move in moveList) {

            board[move.first][move.second] = Model.BoardItem.AI
            if (getWinner(board) == Model.BoardItem.AI) {
                board[move.first][move.second] = Model.BoardItem.Empty
                return move
            }

            board[move.first][move.second] = Model.BoardItem.Player
            if (getWinner(board) == Model.BoardItem.Player) {
                board[move.first][move.second] = Model.BoardItem.Empty
                return move
            }
            board[move.first][move.second] = Model.BoardItem.Empty

        }

        return moveList.random()

    }

    fun getMove(board: Array<Array<Model.BoardItem>>): Pair<Int, Int> {

        if (difficulty == Difficulty.Hard) {
            val lastMove: Pair<Int, Int> = Pair(0, 0);
            return findBestMoveHard(board, Model.BoardItem.AI, 0, lastMove).first;
        } else if (difficulty == Difficulty.Medium) {
            val lastMove: Pair<Int, Int> = Pair(0, 0);
            return findBestMoveMedium(board);
        } else {
            return getMoveList(board).random()
        }

    }

}
