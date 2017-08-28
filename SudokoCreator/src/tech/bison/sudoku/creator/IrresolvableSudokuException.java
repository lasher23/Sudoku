package tech.bison.sudoku.creator;

public class IrresolvableSudokuException extends Exception {

  private static final long serialVersionUID = 1L;

  public IrresolvableSudokuException(String message) {
    super(message);
  }

}
