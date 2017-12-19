package tech.bison.sudoku.creator;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class SudokuTest {

  @Test
  public void arrayDoesNotContain3() throws IrresolvableSudokuException {
    Sudoku sudoku = new Sudoku(26);
    int[][] testData = new int[][] { { 1, 2, 0, 0, 0, 0, 0, 0, 0 }, { 4, 5, 0, 0, 0, 0, 0, 0, 0 },
        { 0, 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0, 0 },
        { 0, 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0, 0 },
        { 0, 0, 0, 0, 0, 0, 0, 0, 0 } };
    writeMatrix(testData);
    assertThat(sudoku.legal(testData, 3, 0, 2), is(true));
  }

  @Test
  public void arrayContains2() throws IrresolvableSudokuException {
    Sudoku sudoku = new Sudoku(26);
    int[][] testData = new int[][] { { 0, 1, 3, 0, 0, 0, 0, 0, 2 }, { 4, 5, 0, 0, 0, 0, 0, 0, 0 },
        { 0, 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0, 0 },
        { 0, 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0, 0 },
        { 0, 0, 0, 0, 0, 0, 0, 0, 0 } };
    writeMatrix(testData);
    assertThat(sudoku.legal(testData, 2, 0, 0), is(false));
  }

  @Test
  public void arrayDoesContains5() throws IrresolvableSudokuException {
    Sudoku sudoku = new Sudoku(26);
    int[][] testData = new int[][] { { 0, 2, 3, 0, 0, 0, 0, 0, 0 }, { 4, 5, 0, 0, 0, 0, 0, 0, 0 },
        { 0, 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0, 0 },
        { 0, 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0, 0 },
        { 0, 0, 0, 0, 0, 0, 0, 0, 0 } };
    writeMatrix(testData);
    assertThat(sudoku.legal(testData, 5, 0, 0), is(false));
  }

  @Test
  public void arrayDoesNotContains6() throws IrresolvableSudokuException {
    Sudoku sudoku = new Sudoku(30);
    int[][] testData = new int[][] { { 0, 2, 3, 0, 0, 0, 0, 0, 0 }, { 4, 5, 0, 0, 6, 0, 0, 0, 0 },
        { 0, 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0, 0 },
        { 0, 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0, 0 },
        { 0, 0, 0, 0, 0, 0, 0, 0, 0 } };
    writeMatrix(testData);
    assertThat(sudoku.legal(testData, 6, 0, 0), is(true));
  }

  @Test
  public void createSudoku() throws Exception {
    Sudoku sudoku = new Sudoku(50);
    sudoku.solve();
    writeMatrix(sudoku.getPuzzle());
    assertThat(sudoku.solve(0, 0, sudoku.getPuzzle(), (byte) 0, false), is((byte) 1));
  }

  @Test
  public void correctSudokuIsCorrectlySolved() throws IrresolvableSudokuException {
    Sudoku sudoku = new Sudoku(0);
    int[][] sudokuData = { { 7, 3, 6, 4, 8, 9, 2, 1, 5 }, { 1, 4, 8, 5, 2, 6, 3, 9, 7 }, { 2, 5, 9, 3, 1, 7, 4, 6, 8 },
        { 6, 7, 3, 8, 4, 1, 5, 2, 9 }, { 8, 2, 4, 6, 9, 5, 7, 3, 1 }, { 9, 1, 5, 7, 3, 2, 6, 8, 4 },
        { 3, 8, 1, 2, 5, 4, 9, 7, 6 }, { 4, 6, 2, 9, 7, 8, 1, 5, 3 }, { 5, 9, 7, 1, 6, 3, 8, 4, 2 } };

    assertThat(sudoku.isValidSolution(sudokuData), is(true));
  }

  // @Test
  // public void solveSolvesSudokuAlreadyFilled() throws IrresolvableSudokuException {
  // Sudoku sudoku = new Sudoku(10);
  //
  // sudoku.generate();
  // sudoku.solve(0, 0, sudoku.getPuzzle(), (byte) 0, true);
  //
  // assertThat(sudoku.isValidSolution(sudoku.getPuzzle()), is(true));
  // }

  @Test
  public void solveSolvesSudokuAlreadyFilledCorrectly() throws IrresolvableSudokuException {
    Sudoku sudoku = new Sudoku(10);

    sudoku.generate();
    sudoku.solve(0, 0, sudoku.getPuzzle());

    assertThat(sudoku.isValidSolution(sudoku.getPuzzle()), is(true));
  }

  static void writeMatrix(int[][] solution) {
    for (int i = 0; i < 9; ++i) {
      if (i % 3 == 0) {
        if (i == 0) {
          System.out.println("╔�?�?�?╤�?�?�?╤�?�?�?╦�?�?�?╤�?�?�?╤�?�?�?╦�?�?�?╤�?�?�?╤�?�?�?╗");
        } else {
          System.out.println("╠�?�?�?╪�?�?�?╪�?�?�?╬�?�?�?╪�?�?�?╪�?�?�?╬�?�?�?╪�?�?�?╪�?�?�?╣");
        }
      } else {
        System.out.println("╟───┼───┼───╫───┼───┼───╫───┼───┼───╢");
      }
      for (int j = 0; j < 9; ++j) {
        if (j % 3 == 0) {
          System.out.print("║");
        } else {
          System.out.print("│");
        }
        System.out.print(" ");
        System.out.print((solution[i][j] == 0 ? " " : Integer.toString(solution[i][j])));
        System.out.print(" ");

      }
      System.out.println("║");
    }
    System.out.println("╚�?�?�?╧�?�?�?╧�?�?�?╩�?�?�?╧�?�?�?╧�?�?�?╩�?�?�?╧�?�?�?╧�?�?�?�?");
  }
}
