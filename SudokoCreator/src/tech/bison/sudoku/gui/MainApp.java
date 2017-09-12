package tech.bison.sudoku.gui;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Optional;

import com.itextpdf.text.DocumentException;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import tech.bison.sudoku.creator.Sudoku;
import tech.bison.sudoku.csv.ExportCSV;
import tech.bison.sudoku.csv.ImportCSV;
import tech.bison.sudoku.gui.view.RootLayoutController;
import tech.bison.sudoku.gui.view.SudokuGrid;
import tech.bison.sudoku.gui.view.ViewUtils;
import tech.bison.sudoku.pdf.ExportToPDF;
import tech.bison.sudoku.qrcode.ExportQRCode;

public class MainApp extends Application {

	private Stage primaryStage;
	private BorderPane rootLayout;
	private SudokuGrid sudokuGrid;
	private AnchorPane start;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("Sudoku");
		this.primaryStage.setResizable(false);
		primaryStage.setOnCloseRequest(value -> {
			Platform.exit();
			System.exit(0);
		});

		this.primaryStage.getIcons().add(ViewUtils.SUDOKU_ICON_32);

		initRootLayout();
		initStartPage();
	}

	public void showSudokuGrid() {
		SudokuGrid sudokuGrid = new SudokuGrid(this);
		GridPane pane = sudokuGrid.createNewSudoku();
		if (pane != null) {
			this.sudokuGrid = sudokuGrid;
			rootLayout.setCenter(pane);
		}
	}

	public void checkSudoku() {
		if (checkIfSudokuIsCreated()) {
			sudokuGrid.check();
		}
	}

	public Stage getPrimaryStage() {
		return primaryStage;
	}

	public void saveSudokuDataToFile() {
		if (!checkIfSudokuIsCreated()) {
			return;
		}
		FileChooser fileChooser = createCSVFileChooser();
		File file = fileChooser.showSaveDialog(getPrimaryStage());
		if (file != null) {
			if (!file.getPath().endsWith(".csv")) {
				file = new File(file.getPath() + ".csv");
			}
			sudokuGrid.readIntoPuzzle();
			createCSV(sudokuGrid.getSudoku(), file.toString());
		}
	}

	public void loadFromCSV() {
		int[][] puzzle;
		FileChooser fileChooser = createCSVFileChooser();
		File file = fileChooser.showOpenDialog(getPrimaryStage());
		if (file == null) {
			return;
		}
		try {
			puzzle = new ImportCSV(file.toPath()).read();
		} catch (Exception e) {
			ViewUtils.errorAlert("Error during import " + e.toString()).showAndWait();
			return;
		}
		sudokuGrid = new SudokuGrid(this);
		GridPane pane = sudokuGrid.loadSudoku(puzzle);
		rootLayout.setCenter(pane);
	}

	public void solveSudoku() {
		if (!checkIfSudokuIsCreated()) {
			return;
		}
		sudokuGrid.solveAndPrint();
	}

	public void clearSudoku() {
		if (checkIfSudokuIsCreated()) {
			sudokuGrid.clear();
		}
	}

	private void initStartPage() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/Start.fxml"));
			start = (AnchorPane) loader.load();

			rootLayout.setCenter(start);
		} catch (IOException e) {
		}
	}

	private void initRootLayout() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/RootLayout.fxml"));
			rootLayout = (BorderPane) loader.load();

			RootLayoutController controller = loader.getController();
			controller.setMain(this);

			Scene scene = new Scene(rootLayout);
			primaryStage.setScene(scene);
			scene.getStylesheets().add(ViewUtils.SUDOKU_CSS);
			scene.getStylesheets().add(ViewUtils.MATERIAL_FX_CSS);
			primaryStage.show();

		} catch (IOException e) {
		}
	}

	private boolean checkIfSudokuIsCreated() {
		if (sudokuGrid == null) {
			ViewUtils.errorAlert("You have to create a Sudoku first!").showAndWait();
			return false;
		}
		return true;
	}

	private void createCSV(Sudoku sudoku, String path) {
		try {
			new ExportCSV(sudoku, Paths.get(path)).export();
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setHeaderText("Exported");
			alert.setContentText("Sudoku has succesfully been exported!");
			alert.showAndWait();
		} catch (IOException e) {
			ViewUtils.errorAlert("Error during export " + e.toString()).showAndWait();
		}
	}

	private FileChooser createCSVFileChooser() {
		FileChooser fileChooser = new FileChooser();
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("CSV file (*.csv)", "*.csv");
		fileChooser.getExtensionFilters().add(extFilter);
		return fileChooser;
	}

	public void exportToPDF() {
		FileChooser fileChooser = new FileChooser();
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("PDF file (*.pdf)", "*.pdf");
		fileChooser.getExtensionFilters().add(extFilter);
		File file = fileChooser.showSaveDialog(getPrimaryStage());
		try {
			ExportToPDF.print(file.toString(), sudokuGrid.getPuzzle(), sudokuGrid.getDifficulty());
		} catch (FileNotFoundException | DocumentException e) {
			ViewUtils.errorAlert("Error during export" + e.toString()).showAndWait();
		} catch (NullPointerException e) {
			ViewUtils.errorAlert("Sudoku must be created first").showAndWait();
		}
	}

	public void exportToQRCode() {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Options");
		alert.setHeaderText("Diffrent actions are posssible.");
		alert.setContentText("Choose your option.");

		ButtonType buttonTypeOne = new ButtonType("Show the Picture");
		ButtonType buttonTypeTwo = new ButtonType("Save as JPG");
		ButtonType buttonTypeThree = new ButtonType("Both");
		ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);

		alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeTwo, buttonTypeThree, buttonTypeCancel);

		Optional<ButtonType> result = alert.showAndWait();
		ExportQRCode exportQRCode;
		try {
			exportQRCode = new ExportQRCode(sudokuGrid.getPuzzle());
		} catch (NullPointerException e) {
			ViewUtils.errorAlert("Can't export a empty sudoku!");
			return;
		}
		if (result.get() == buttonTypeOne) {
			showQRCodeAsAlert(exportQRCode);
		} else if (result.get() == buttonTypeTwo) {
			saveQRCodeToJPG(exportQRCode);
		} else if (result.get() == buttonTypeThree) {
			saveQRCodeToJPG(exportQRCode);
			showQRCodeAsAlert(exportQRCode);
		}
	}

	private void saveQRCodeToJPG(ExportQRCode exportQRCode) {
		FileChooser fileChooser = new FileChooser();
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("JPG file (*.jpg)", "*.jpg");
		fileChooser.getExtensionFilters().add(extFilter);
		File file = fileChooser.showSaveDialog(getPrimaryStage());
		try {
			exportQRCode.export(file.toPath());
		} catch (IOException e) {
			ViewUtils.errorAlert("Error while saving to file.").showAndWait();
		}
	}

	private void showQRCodeAsAlert(ExportQRCode exportQRCode) {
		WritableImage fxImage = SwingFXUtils.toFXImage((BufferedImage) exportQRCode.getAWTImage(), null);

		Alert imgAlert = new Alert(AlertType.INFORMATION);
		imgAlert.setGraphic(new ImageView(fxImage));
		imgAlert.setTitle("QRCode");
		imgAlert.setHeaderText("The QR Code of your Sudoku:");
		imgAlert.showAndWait();
	}
}
