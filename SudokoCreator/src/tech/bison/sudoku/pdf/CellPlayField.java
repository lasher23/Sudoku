package tech.bison.sudoku.pdf;

import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;

public class CellPlayField {
  private PdfPTable table;
  private final int[][] values;

  public CellPlayField(int[][] values) {
    this.values = values;
    table = new PdfPTable(9);
    addCellsToTable();
  }

  public PdfPTable getTable() {
    return table;
  }

  private void addCellsToTable() {
    for (int i = 0; i < 9; i++) {
      for (int j = 0; j < 9; j++) {
        PdfPCell cell = new PdfPCell();
        cell.setMinimumHeight(50);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        if (values[i][j] != 0) {
          cell.addElement(new Paragraph(String.valueOf(values[i][j])));
        }
        table.addCell(cell);
      }
    }
  }
}
