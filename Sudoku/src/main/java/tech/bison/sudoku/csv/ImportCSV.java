package tech.bison.sudoku.csv;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import tech.bison.ints.Ints;

public class ImportCSV {
  private BufferedReader br;

  public ImportCSV(Path path) throws IOException {
    br = Files.newBufferedReader(path);
  }

  public int[][] read() throws IOException {
    int[][] array = new int[9][];
    for (int i = 0; i < 9; i++) {
      array[i] = Ints.split(";", br.readLine());
    }
    br.close();
    return array;
  }
}
