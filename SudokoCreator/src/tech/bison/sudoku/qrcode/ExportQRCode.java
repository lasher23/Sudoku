package tech.bison.sudoku.qrcode;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import javax.imageio.ImageIO;

import com.itextpdf.text.pdf.BarcodeQRCode;

public class ExportQRCode {
  private int[][] puzzle;
  private StringBuilder csvFormat = new StringBuilder();
  private BarcodeQRCode qrCode = null;
  private Image imgQRCode;

  public ExportQRCode(int[][] puzzle) {
    this.puzzle = puzzle;
    createCSVString();
    this.qrCode = new BarcodeQRCode(csvFormat.toString(), 100, 100, null);
    imgQRCode = qrCode.createAwtImage(Color.BLACK, new Color(244, 244, 244));
  }

  public BufferedImage getAWTImage() {
    return toBufferedImage(imgQRCode);
  }

  public boolean export(Path destination) throws IOException {
    File file = new File(destination.toString());
    return ImageIO.write(toBufferedImage(imgQRCode), "jpg", file);
  }

  private void createCSVString() {
    for (int i = 0; i < 9; i++) {
      for (int j = 0; j < 9; j++) {
        csvFormat.append(puzzle[i][j]);
      }
      csvFormat.append('\n');
    }
  }

  private static BufferedImage toBufferedImage(Image src) {
    int w = src.getWidth(null);
    int h = src.getHeight(null);
    int type = BufferedImage.TYPE_INT_RGB;
    BufferedImage dest = new BufferedImage(w, h, type);
    Graphics2D g2 = dest.createGraphics();
    g2.drawImage(src, 0, 0, null);
    g2.dispose();
    return dest;
  }
}
