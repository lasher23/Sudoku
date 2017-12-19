package tech.bison.sudoku.gui.view;

import java.util.EmptyStackException;
import java.util.Stack;

public class SudokuHistory {
	private Stack<SudokuHistoryElement> undoElements = new Stack<>();
	private Stack<SudokuHistoryElement> redoElements = new Stack<>();

	public void addToHistory(SudokuHistoryElement element) {
		undoElements.push(element);
	}

	public SudokuHistoryElement getLastStateAndDelete() {
		return undoElements.pop();
	}

	public SudokuHistoryElement getLastState() {
		return undoElements.peek();
	}

	public boolean hasElements() {
		return undoElements.size() > 0;
	}

	public void redo() {
		try {
			SudokuHistoryElement element = redoElements.pop();
			element.getTf().setText(element.getValue());
			element.getTf().requestFocus();
		} catch (EmptyStackException e) {
			// do nothing if the stack is empty
		}
	}

	public void undo() {
		try {
			SudokuHistoryElement element = undoElements.peek();
			redoElements.push(new SudokuHistoryElement(element.getTf(), element.getTf().getText()));
			if (element.getTf().isEditable()) {
				element.getTf().setText(element.getValue());
				element.getTf().requestFocus();
				undoElements.pop();
			}
		} catch (EmptyStackException e) {
			// do nothing if the stack is empty
		}
	}
}
