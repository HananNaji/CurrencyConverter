package application;

public class TicTacToeGame {
    public int[][] board = new int[3][3]; // 0 = فارغ، 1 = X، 2 = O

    public int[][] getBoard() {
        return board;
    }

    public void setBoard(int[][] board) {
        this.board = board;
    }

    public TicTacToeGame() {
        // تهيئة اللوحة
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board[i][j] = 0;
            }
        }
    }

    // دالة للتحقق من الفوز
    public boolean checkWin(int player) {
        // تحقق من الصفوف والأعمدة والقطريين
        for (int i = 0; i < 3; i++) {
            if (board[i][0] == player && board[i][1] == player && board[i][2] == player)
                return true;
            if (board[0][i] == player && board[1][i] == player && board[2][i] == player)
                return true;
        }
        if (board[0][0] == player && board[1][1] == player && board[2][2] == player)
            return true;
        if (board[0][2] == player && board[1][1] == player && board[2][0] == player)
            return true;

        return false;
    }

    // دالة للتحقق من أن اللعبة انتهت
    public boolean isBoardFull() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == 0) {
                    return false;
                }
            }
        }
        return true;
    }

    public void makeMove(int x, int y, int player) {
        if (board[x][y] == 0) {
            board[x][y] = player;  // تعيين الحركات
        }
    }

    // دالة لعرض اللوحة
    public void displayBoard() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }
    }
}
