package tech.bison.sudoku.gui.view;

public class HistoryElement {
  private int positionX;
  private int positionY;
  private int value;

  public HistoryElement(int value, int positionX, int positionY) {
    this.value = value;
    this.positionX = positionX;
    this.positionY = positionY;
  }

  public int getPositionX() {
    return positionX;
  }

  public int getPositionY() {
    return positionY;
  }

  public int getValue() {
    return value;
  }
}
