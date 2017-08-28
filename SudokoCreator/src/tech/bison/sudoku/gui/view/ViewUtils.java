package tech.bison.sudoku.gui.view;

import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class ViewUtils {
  public static final String MATERIAL_FX_CSS = "tech/bison/sudoku/gui/view/material-fx.css";
  public static final String SUDOKU_CSS = "tech/bison/sudoku/gui/view/sudokulayout.css";
  public static final Image SUDOKU_ICON_32 = new Image("tech/bison/sudoku/gui/view/SudokuIcon32.png");

  public static Alert errorAlert(String contentText) {
    Alert alert = new Alert(Alert.AlertType.ERROR);
    alert.setHeaderText("Error");
    alert.getDialogPane().getStylesheets().add(MATERIAL_FX_CSS);
    alert.setContentText(contentText);
    Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
    stage.getIcons().add(SUDOKU_ICON_32);
    return alert;
  }
}
