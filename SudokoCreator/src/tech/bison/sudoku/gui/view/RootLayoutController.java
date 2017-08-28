package tech.bison.sudoku.gui.view;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javafx.fxml.FXML;
import tech.bison.sudoku.gui.MainApp;

public class RootLayoutController {
  private MainApp main;

  public void setMain(MainApp main) {
    this.main = main;
  }

  @FXML
  private void handleNewSudoku() {
    main.showSudokuGrid();
  }

  @FXML
  private void handleCheckButton() {
    main.checkSudoku();
  }

  @FXML
  private void handleSaveAs() {
    main.saveSudokuDataToFile();
  }

  @FXML
  private void handleHelp() {
    if (Desktop.isDesktopSupported()) {
      try {
        Desktop.getDesktop().browse(new URI("https://en.wikipedia.org/wiki/Sudoku"));
      } catch (IOException e) {
      } catch (URISyntaxException e) {
      }
    }
  }

  @FXML
  private void handleImport() {
    main.loadFromCSV();
  }

  @FXML
  private void handleSolve() {
    main.solveSudoku();
  }

  @FXML
  private void handleClear() {
    main.clearSudoku();
  }

  @FXML
  private void handleExportPDF() {
    main.exportToPDF();
  }

  @FXML
  private void handleExportQRCode() {
    main.exportToQRCode();
  }
}
