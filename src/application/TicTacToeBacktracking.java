package application;

public class TicTacToeBacktracking {
    private TicTacToeGame game;

    // ✅ Constructor يستقبل اللعبة
    public TicTacToeBacktracking(TicTacToeGame game) {
        this.game = game;
    }

    public TicTacToeGame getGame() {
        return game;
    }

    public void setGame(TicTacToeGame game) {
        this.game = game;
    }

    public static class Move {
        int row, col;

        public Move(int row, int col) {
            this.row = row;
            this.col = col;
        }
    }

    public Move tryNextMove(int player) {
        Move bestMove = null;

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (game.board[i][j] == 0) {
                    game.makeMove(i, j, player);

                    if (game.checkWin(player)) {
                        game.board[i][j] = 0;
                        return new Move(i, j);
                    }

                    int opponent = (player == 1) ? 2 : 1;
                    if (!game.isBoardFull()) {
                        Move response = tryNextMove(opponent);
                        if (response != null) {
                            bestMove = new Move(i, j);
                        }
                    }

                    game.board[i][j] = 0;
                }
            }
        }

        return bestMove;
    }
}
