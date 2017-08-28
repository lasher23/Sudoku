package tech.bison.sudoku.gui.view;

import java.util.LinkedList;
import java.util.Queue;

public class SudokuHistory {
  private Queue<HistoryElement> elements = new LinkedList<>();

  public void addToHistory(HistoryElement element) {
    System.out.println("add element");
    elements.add(element);
  }

  public HistoryElement getLastStateAndDelete() {
    return elements.poll();
  }

  public HistoryElement getLastState() {
    System.out.println("getlast");
    return elements.peek();
  }

  public boolean hasElements() {
    return elements.size() > 0;
  }
}
