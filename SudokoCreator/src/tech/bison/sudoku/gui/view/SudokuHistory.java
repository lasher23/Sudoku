package tech.bison.sudoku.gui.view;

import java.util.Stack;

public class SudokuHistory {
	private Stack<SudokuHistoryElement> elements = new Stack<>();

	public void addToHistory(SudokuHistoryElement element) {
		elements.push(element);
	}

	public SudokuHistoryElement getLastStateAndDelete() {
		return elements.pop();
	}

	public SudokuHistoryElement getLastState() {
		return elements.peek();
	}

	public boolean hasElements() {
		return elements.size() > 0;
	}

	public void undo() {
		SudokuHistoryElement element = elements.peek();
		if (!element.getTf().isDisabled()) {
			element.getTf().setText(element.getValue());
			element.getTf().requestFocus();
			elements.pop();
		}
	}
}
