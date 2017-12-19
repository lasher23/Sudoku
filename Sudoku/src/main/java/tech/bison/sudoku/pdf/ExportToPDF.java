package tech.bison.sudoku.pdf;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

public class ExportToPDF {
  public static void print(String path, int[][] values, int difficulty)
      throws FileNotFoundException, DocumentException {
    CellPlayField cellPlayField = new CellPlayField(values);
    Document document = new Document();
    PdfWriter.getInstance(document, new FileOutputStream(path));
    document.open();
    document.add(new Paragraph("Sudoku der Schwierigkeit: " + difficulty));
    document.add(Chunk.NEWLINE);
    document.add(cellPlayField.getTable());
    document.close();
  }
}
