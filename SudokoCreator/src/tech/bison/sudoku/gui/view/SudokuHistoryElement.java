package tech.bison.sudoku.gui.view;

import javafx.scene.control.TextField;

public class SudokuHistoryElement {
	private String value;
	private TextField tf;

	public SudokuHistoryElement(TextField tf, String value) {
		this.tf = tf;
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public TextField getTf() {
		return tf;
	}
}
