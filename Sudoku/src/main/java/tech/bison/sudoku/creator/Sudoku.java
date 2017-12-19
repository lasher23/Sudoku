package tech.bison.sudoku.creator;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

import tech.bison.ints.Ints;

public class Sudoku {
  private static final int FIELD_WIDTH = 9;
  private static final int TRIES = 1_000_000;
  private static final int THREAD_COUNT = 128;
  private static final int TRIES_PER_THREAD = TRIES / THREAD_COUNT;
  private static final int CELL_COUNT = FIELD_WIDTH * FIELD_WIDTH;
  private final int holes;
  private int[][] puzzle;
  private long seedUniquifier = 8682522807148012L;

  public Sudoku(int holes) throws IrresolvableSudokuException {
    if (holes > CELL_COUNT - 17) {
      throw new IrresolvableSudokuException("A sudoku with more than 64 empty cells can't be generated");
    }
    this.holes = holes;
    puzzle = new int[FIELD_WIDTH][FIELD_WIDTH];
  }

  public void generate() {
    puzzle = new int[FIELD_WIDTH][FIELD_WIDTH];
    solve(0, 0, puzzle, (byte) 0, true);
    removeNums(puzzle);
  }

  public void solve() {
    solve(0, 0, puzzle);
  }

  public int[][] getPuzzle() {
    return puzzle;
  }

  public boolean isValidSolution(int[][] solution) {
    for (int i = 0; i < 9; i++) {
      for (int j = 0; j < 9; j++) {
        if (solution[i][j] < 1 || solution[i][j] > 9 || !isValid(i, j, solution)) {
          return false;
        }
      }
    }
    return true;
  }

  boolean legal(int[][] cells, int num, int x, int y) {
    for (int i = 0; i < FIELD_WIDTH; i++) {
      if (cells[i][x] == num || cells[y][i] == num) {
        return false;
      }
    }
    int yOffset = (y / 3) * 3;
    int xOffset = (x / 3) * 3;
    int maxYOffset = yOffset + 3;
    for (int k = yOffset; k < maxYOffset; k++) {
      if (num == cells[k][xOffset] || num == cells[k][xOffset + 1] || num == cells[k][xOffset + 2]) {
        return false;
      }
    }
    return true;
  }

  byte solve(int i, int j, int[][] cells, byte count, boolean random) {
    if (i == FIELD_WIDTH) {
      i = 0;
      if (++j == FIELD_WIDTH) {
        return ++count;
      }
    }
    if (cells[i][j] != 0) {
      return solve(i + 1, j, cells, count, random);
    }
    int val = random ? Math.max(1, (int) (randomDouble() * 9)) : 1;
    for (int n = 0; n <= 9 && count <= 1; n++) {
      if (legal(cells, val, j, i)) {
        cells[i][j] = val;
        count = solve(i + 1, j, cells, count, random);
      }
      val = val == 9 ? 1 : val + 1;
    }
    if (count <= 1) {
      cells[i][j] = 0;
    }
    return count;
  }

  boolean solve(int i, int j, int[][] cells) {
    if (i == FIELD_WIDTH) {
      i = 0;
      if (++j == FIELD_WIDTH) {
        return true;
      }
    }
    if (cells[i][j] != 0) {
      return solve(i + 1, j, cells);
    }
    for (int val = 1; val <= FIELD_WIDTH; ++val) {
      if (legal(cells, val, j, i)) {
        cells[i][j] = val;
        if (solve(i + 1, j, cells)) {
          return true;
        }
      }
    }
    cells[i][j] = 0;
    return false;
  }

  private int[][] removeNums(int[][] cells) {
    ExecutorService threadPool = Executors.newWorkStealingPool(THREAD_COUNT);
    AtomicBoolean keepRunning = new AtomicBoolean(true);
    Solution solution = new Solution();
    Runnable r = () -> {
      int tries = TRIES_PER_THREAD;
      while (tries-- > 0 && keepRunning.get()) {
        int[][] tmp = remover(cells);
        if (solve(0, 0, Ints.copy(tmp), (byte) 0, false) == 1) {
          solution.solution = tmp;
          solution.solved = true;
          return;
        }
      }
    };
    for (int i = 0; i <= THREAD_COUNT; i++) {
      threadPool.submit(r);
    }
    threadPool.shutdown();
    while (!solution.solved && !threadPool.isTerminated()) {
      // This isn't beautiful code but it seems to be the easiest way to do things
    }
    threadPool.shutdownNow();
    keepRunning.set(false);
    puzzle = solution.solution;
    return solution.solution;
  }

  private boolean isValid(int i, int j, int[][] grid) {
    for (int column = 0; column < 9; column++) {
      if (column != j && grid[i][column] == grid[i][j]) {
        return false;
      }
    }
    for (int row = 0; row < 9; row++) {
      if (row != i && grid[row][j] == grid[i][j]) {
        return false;
      }
    }
    for (int row = (i / 3) * 3; row < (i / 3) * 3 + 3; row++) {
      for (int col = (j / 3) * 3; col < (j / 3) * 3 + 3; col++) {
        if (row != i && col != j && grid[row][col] == grid[i][j]) {
          return false;
        }
      }
    }
    return true;
  }

  private int[][] remover(int[][] cells) {
    int[][] temp = Ints.copy(cells);
    double remainingCells = CELL_COUNT;
    int remainingHoles = holes;
    for (int i = 0; i < FIELD_WIDTH; i++) {
      for (int j = 0; j < FIELD_WIDTH; j++) {
        if (randomDouble() <= remainingHoles / remainingCells
            && (Ints.multiOccurence(temp, temp[i][j]) || Ints.hasMoreDistinct(temp, 8))) {
          temp[i][j] = 0;
          remainingHoles--;
        }
        remainingCells--;
      }
    }
    return temp;
  }

  private double randomDouble() {
    long x = System.currentTimeMillis() * ++seedUniquifier;
    x ^= (x << 21);
    x ^= (x >>> 35);
    x ^= (x << 4);
    return Math.abs((((int) x >>> 57) / (double) 100) - 1);
  }

  private class Solution {
    public int[][] solution;
    public boolean solved;
  }
}
