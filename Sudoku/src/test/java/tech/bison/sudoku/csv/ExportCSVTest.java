package tech.bison.sudoku.csv;

import java.io.File;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import tech.bison.sudoku.creator.Sudoku;

public class ExportCSVTest {
  @Rule
  public TemporaryFolder temporaryFolder = new TemporaryFolder();

  @Test
  public void test() throws Exception {
    File file = temporaryFolder.newFile();
    Sudoku sudoku = new Sudoku(20);

    sudoku.generate();

    ExportCSV exportCSV = new ExportCSV(sudoku, file.toPath());
    exportCSV.export();
  }
}
