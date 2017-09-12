package tech.bison.sudoku.gui.view;

import java.util.Optional;

import org.controlsfx.dialog.ProgressDialog;

import javafx.beans.value.ChangeListener;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.css.PseudoClass;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import tech.bison.sudoku.creator.IrresolvableSudokuException;
import tech.bison.sudoku.creator.Sudoku;
import tech.bison.sudoku.gui.MainApp;

public class SudokuGrid {
	private static final int GRID_SIZE = 9;
	private VBox box = new VBox();
	private GridPane gridPane = new GridPane();
	private TextField[][] cells = new TextField[GRID_SIZE][GRID_SIZE];
	private int[][] puzzle = new int[GRID_SIZE][GRID_SIZE];
	private int difficulty;
	private Sudoku sudoku;
	private MainApp main;
	private SudokuHistory history = new SudokuHistory();
	private boolean changedFromHistory = false;
	private Timer timer = new Timer();

	public SudokuGrid(MainApp main) {
		this.main = main;
		gridPane.getStylesheets().add(ViewUtils.SUDOKU_CSS);
		gridPane.getStyleClass().add("border");
	}

	public int[][] getPuzzle() {
		return puzzle;
	}

	public VBox createNewSudoku() {
		if (showDifficultyDialog()) {
			initCells();
			addCellsToAnchorPane();
			addSudoku();
			printPuzzle();
			box.getChildren().add(gridPane);

			timer.startTimer();
			box.getChildren().add(timer);
			box.getChildren().add(new TextField());
			return box;
		}
		return null;
	}

	public void clear() {
		for (int row = 0; row < GRID_SIZE; ++row) {
			for (int col = 0; col < GRID_SIZE; ++col) {
				if (cells[row][col].isEditable()) {
					cells[row][col].setText("");
				}
			}
		}
	}

	public GridPane loadSudoku(int[][] puzzle) {
		this.puzzle = puzzle;
		initCells();
		addCellsToAnchorPane();
		printPuzzle();
		return gridPane;
	}

	public void check() {
		if (!readIntoPuzzleShowAlert()) {
			return;
		}
		if (sudoku.isValidSolution(puzzle)) {
			timer.stopTimer();
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.getDialogPane().getStylesheets().add(ViewUtils.MATERIAL_FX_CSS);
			alert.setHeaderText("Congratulation");
			alert.setContentText("You have solved the sudoku correctly!");
			alert.showAndWait();

			Alert alertPlayAgain = new Alert(AlertType.CONFIRMATION);
			alertPlayAgain.getDialogPane().getStylesheets().add(ViewUtils.MATERIAL_FX_CSS);
			alertPlayAgain.setTitle("Play again?");
			alertPlayAgain.setHeaderText("Play again?");
			alertPlayAgain.setContentText("Do you want to play an other game?");

			Optional<ButtonType> result = alertPlayAgain.showAndWait();
			if (result.get() == ButtonType.OK) {
				main.showSudokuGrid();
			}
		} else {
			ViewUtils.errorAlert("The sudoku isn't solved correctly!").showAndWait();
		}
	}

	public void solveAndPrint() {
		readIntoPuzzle();
		sudoku.solve();
		puzzle = sudoku.getPuzzle();
		printPuzzle();
	}

	public int readIntoPuzzle() {
		int empty = 0;
		for (int row = 0; row < GRID_SIZE; ++row) {
			for (int col = 0; col < GRID_SIZE; ++col) {
				String str = cells[row][col].getText();
				if ("".equals(str)) {
					empty++;
					puzzle[row][col] = 0;
				} else {
					puzzle[row][col] = Integer.parseInt(str);
				}
			}
		}
		return empty;
	}

	public Sudoku getSudoku() {
		return sudoku;
	}

	private boolean showDifficultyDialog() {
		TextInputDialog dialog = new TextInputDialog();
		dialog.getDialogPane().getStylesheets().add(ViewUtils.MATERIAL_FX_CSS);
		dialog.setTitle("Set difficulty");
		dialog.setHeaderText("How difficult shoud the Sudoku be?");
		dialog.setContentText("Enter the amount of holes: ");
		Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();
		stage.getIcons().add(ViewUtils.SUDOKU_ICON_32);
		while (true) {
			Optional<String> input = dialog.showAndWait();
			if (!input.isPresent()) {
				return false;
			}
			try {
				difficulty = Integer.parseInt(input.get());
				sudoku = new Sudoku(difficulty);
				return true;
			} catch (NumberFormatException e) {
				ViewUtils.errorAlert("The value must be an Integer!").showAndWait();
			} catch (IrresolvableSudokuException e) {
				ViewUtils.errorAlert(e.getMessage()).showAndWait();
			}
		}
	}

	private void initCells() {
		for (int row = 0; row < GRID_SIZE; row++) {
			for (int col = 0; col < GRID_SIZE; col++) {
				TextField tf = new TextField();
				addKeyListener(tf);
				addUndoRedoListener(tf);
				cells[row][col] = tf;
			}
		}

	}

	private void addUndoRedoListener(TextField tf) {
		tf.setOnKeyPressed(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent event) {
				KeyCodeCombination combinationUndo = new KeyCodeCombination(KeyCode.Z, KeyCodeCombination.CONTROL_DOWN);
				KeyCodeCombination combinationRedo = new KeyCodeCombination(KeyCode.Y, KeyCodeCombination.CONTROL_DOWN);
				if (combinationUndo.match(event)) {
					undo();
					event.consume();
				} else if (combinationRedo.match(event)) {
					redo();
					event.consume();
				}
			}
		});
	}

	private void addKeyListener(TextField tf) {
		tf.textProperty().addListener((ChangeListener<String>) (observable, oldValue, newValue) -> {
			if (!newValue.matches("[1-9]") && !"".equals(newValue)) {
				tf.setText(oldValue);
			} else if (!newValue.equals(oldValue)) {
				System.out.println(changedFromHistory);
				if (!changedFromHistory) {
					try {
						history.addToHistory(new SudokuHistoryElement(tf, oldValue));
					} catch (Exception e) {
						System.out.println(e.getMessage());
					}
				}
			}
		});
	}

	private void addCellsToAnchorPane() {
		PseudoClass right = PseudoClass.getPseudoClass("right");
		PseudoClass bottom = PseudoClass.getPseudoClass("bottom");

		for (int row = 0; row < GRID_SIZE; ++row) {
			for (int col = 0; col < GRID_SIZE; ++col) {
				StackPane cell = new StackPane();
				cell.getStyleClass().add("cell");
				cell.pseudoClassStateChanged(right, col == 2 || col == 5);
				cell.pseudoClassStateChanged(bottom, row == 2 || row == 5);

				cell.getChildren().add(cells[row][col]);

				gridPane.add(cell, col, row);
			}
		}
	}

	private void addSudoku() {
		Service<Void> service = new Service<Void>() {
			@Override
			protected Task<Void> createTask() {
				return new Task<Void>() {
					@Override
					protected Void call() throws InterruptedException {
						sudoku.generate();
						puzzle = sudoku.getPuzzle();
						printPuzzle();
						return null;
					}
				};
			}
		};

		ProgressDialog progressDialog = new ProgressDialog(service);
		progressDialog.getDialogPane().getStylesheets().add(ViewUtils.MATERIAL_FX_CSS);
		Stage stage = (Stage) progressDialog.getDialogPane().getScene().getWindow();
		stage.getIcons().add(ViewUtils.SUDOKU_ICON_32);
		progressDialog.show();
		service.start();
	}

	private void printPuzzle() {
		for (int row = 0; row < GRID_SIZE; ++row) {
			for (int col = 0; col < GRID_SIZE; ++col) {
				int number = puzzle[row][col];
				if (number != 0) {
					cells[row][col].setText(String.valueOf(number));
					cells[row][col].setEditable(false);
				}
			}
		}
	}

	private boolean readIntoPuzzleShowAlert() {
		int empty = readIntoPuzzle();
		if (empty > 0) {
			ViewUtils.errorAlert(empty + " cells are still empty!").showAndWait();
			return false;
		}
		return true;
	}

	public int getDifficulty() {
		int difficulty = 0;
		for (int row = 0; row < GRID_SIZE; ++row) {
			for (int col = 0; col < GRID_SIZE; ++col) {
				if (puzzle[row][col] == 0) {
					difficulty++;
				}
			}
		}
		return difficulty;
	}

	private void undo() {
		changedFromHistory = true;
		history.undo();
		changedFromHistory = false;
	}

	private void redo() {
		history.redo();
	}
}
