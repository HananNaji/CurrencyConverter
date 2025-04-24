package application;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.geometry.Pos;

public class TicTacToeApp extends Application {

	private TicTacToeGame game;
	private TicTacToeBacktracking ai;
	private String playerMark = "X";
	private String computerMark = "O";
	private boolean playerTurn = true; // بدأ اللعب من اللاعب
	private ListView<String> markList;
	private ListView<String> startList;
	private GridPane grid;
	private Button[][] buttons;
	private Label winnerLabel;

	@Override
	public void start(Stage primaryStage) {
		game = new TicTacToeGame();
		ai = new TicTacToeBacktracking(game); // ✅ مرر نسخة اللعبة هنا


		grid = new GridPane();
		grid.setAlignment(Pos.CENTER);
		grid.setVgap(10);
		grid.setHgap(10);

		Label title = new Label("Tic Tac Toe");
		title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
		grid.add(title, 0, 0, 3, 1);

		winnerLabel = new Label("");
		winnerLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
		grid.add(winnerLabel, 0, 1, 3, 1);

		Label userVsComputerLabel = new Label("User VS Computer");
		userVsComputerLabel.setStyle("-fx-font-size: 18px;");
		grid.add(userVsComputerLabel, 0, 2, 3, 1);

		Label markLabel = new Label("Choose your mark:");
		markList = new ListView<>(FXCollections.observableArrayList("X", "O"));
		markList.getSelectionModel().select(0);
		markList.setMaxHeight(60);
		grid.add(markLabel, 0, 3);
		grid.add(markList, 1, 3);

		Label startLabel = new Label("Who starts?");
		startList = new ListView<>(FXCollections.observableArrayList("User", "Computer"));
		startList.getSelectionModel().select(0);
		startList.setMaxHeight(60);
		grid.add(startLabel, 0, 4);
		grid.add(startList, 1, 4);

		Button startButton = new Button("Start Game");
		startButton.setOnAction(e -> startGame());
		grid.add(startButton, 0, 5, 2, 1);

		Scene scene = new Scene(grid, 400, 400);
		primaryStage.setTitle("Tic Tac Toe");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	private void startGame() {
		playerMark = markList.getSelectionModel().getSelectedItem();
		computerMark = playerMark.equals("X") ? "O" : "X";
		playerTurn = startList.getSelectionModel().getSelectedItem().equals("User");

		grid.getChildren().clear();
		createGameBoard();

		if (!playerTurn) {
			makeComputerMove(); // يبدأ الكمبيوتر إذا تم اختياره للبداية
		}
	}

	private void createGameBoard() {
		Label boardTitle = new Label("Game Board");
		boardTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
		grid.add(boardTitle, 0, 0, 3, 1);

		buttons = new Button[3][3];
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				final int x = i;
				final int y = j;
				buttons[i][j] = new Button();
				buttons[i][j].setMinSize(100, 100);
				buttons[i][j].setStyle("-fx-font-size: 20px; -fx-background-color: lightgray;");
				buttons[i][j].setOnAction(e -> handleClick(x, y));
				grid.add(buttons[i][j], j, i + 1);
			}
		}
	}

	private void handleClick(int i, int j) {
	    // إذا كانت المربع فارغ وكان دور اللاعب
	    if (game.board[i][j] == 0 && playerTurn) {
	        game.makeMove(i, j, playerMark.equals("X") ? 1 : 2);  // إضافة الحركة
	        buttons[i][j].setText(playerMark);  // وضع "X" أو "O" على المربع
	        playerTurn = false;  // التبديل إلى دور الكمبيوتر بعد حركة اللاعب

	        if (game.checkWin(playerMark.equals("X") ? 1 : 2)) {
	            showWinner("You Win!", "green");
	            return;
	        }

	        if (!game.isBoardFull()) {
	            makeComputerMove();  // إذا كانت اللعبة لم تنتهِ، يقوم الكمبيوتر بحركته
	        }
	    }
	}

	private void makeComputerMove() {
	    TicTacToeBacktracking.Move move = ai.tryNextMove(2);  // 2 = الكمبيوتر
	    if (move != null) {
	        game.makeMove(move.row, move.col, 2);  // الكمبيوتر يضع "O"
	        buttons[move.row][move.col].setText(computerMark);  // عرض "O" في المربع
	        playerTurn = true;  // التبديل إلى دور اللاعب بعد حركة الكمبيوتر

	        if (game.checkWin(2)) {
	            showWinner("Computer Wins!", "red");
	            return;
	        }

	        // تمكين الأزرار الفارغة بعد حركة الكمبيوتر
	        enableButtons();
	    }
	}

	// تمكين الأزرار الفارغة بعد حركة الكمبيوتر
	private void enableButtons() {
	    for (int i = 0; i < 3; i++) {
	        for (int j = 0; j < 3; j++) {
	            // تمكين الأزرار إذا كانت المربعات فارغة
	            if (game.board[i][j] == 0) {
	                buttons[i][j].setDisable(false);  // تمكين الأزرار الفارغة
	            }
	        }
	    }
	}

	private void showWinner(String message, String color) {
	    winnerLabel.setText(message);
	    winnerLabel.setTextFill(javafx.scene.paint.Color.web(color));

	    // ✅ نافذة منبثقة تعرض الرسالة
	    Alert alert = new Alert(Alert.AlertType.INFORMATION);
	    alert.setTitle("Game Over");
	    alert.setHeaderText(null);
	    alert.setContentText(message);
	    alert.showAndWait();

	    // تعطيل الأزرار بعد انتهاء اللعبة
	    for (int i = 0; i < 3; i++) {
	        for (int j = 0; j < 3; j++) {
	            buttons[i][j].setDisable(true);
	        }
	    }
	}


	public static void main(String[] args) {
		launch(args);
	}
}
