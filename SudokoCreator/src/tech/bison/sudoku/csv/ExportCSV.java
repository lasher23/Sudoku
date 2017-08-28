package tech.bison.sudoku.csv;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import ch.deletescape.primitives.arrays.PrIntArray;
import tech.bison.sudoku.creator.Sudoku;

public class ExportCSV {
  private final BufferedWriter bw;
  private final Sudoku sudoku;

  public ExportCSV(Sudoku sudoku, Path path) throws IOException {
    this.sudoku = sudoku;
    bw = Files.newBufferedWriter(path);
  }

  public void export() throws IOException {
    for (int[] is : sudoku.getPuzzle()) {
      bw.write(PrIntArray.join(";", is));
      bw.write("\n");
    }
    bw.close();
  }
}
