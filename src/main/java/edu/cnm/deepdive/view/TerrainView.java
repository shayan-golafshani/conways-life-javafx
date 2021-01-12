package edu.cnm.deepdive.view;

import edu.cnm.deepdive.model.Terrain;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public class TerrainView extends Canvas {

  private static final double MAX_HUE = 360;
  private static final double MAX_SATURATION = 1;
  private static final double MAX_BRIGHTNESS = 1;
  private static final double DEFAULT_HUE = 120;
  private static final double DEFAULT_SATURATION = 1;
  private static final double DEFAULT_NEW_BRIGHTNESS = 1;
  private static final double DEFAULT_OLD_BRIGHTNESS = 0.6;

  private double hue = DEFAULT_HUE;
  private double saturation = DEFAULT_SATURATION;
  private double newBrightness = DEFAULT_NEW_BRIGHTNESS;
  private double oldBrightness = DEFAULT_OLD_BRIGHTNESS;
  private boolean colorsUpdated = false;
  private byte[][] cells;
  private Color[] cellColors;
  private Color backgroundColor;
  private Terrain terrain;
  private WritableImage buffer;
  private PixelWriter writer;
  private boolean bound;

  @Override
  public boolean isResizable() {
    if (!bound) {
      widthProperty().bind(((Pane) getParent()).widthProperty());
      heightProperty().bind(((Pane) getParent()).heightProperty());
      bound = true;
    }
    return true;
  }

  @Override
  public void resize(double width, double height) {
    super.resize(width, height);
    if (terrain != null) {
      draw();
    }
  }

  public double getHue() {
    return hue;
  }

  public void setHue(double hue) {
    this.hue = hue;
  }

  public double getSaturation() {
    return saturation;
  }

  public void setSaturation(double saturation) {
    this.saturation = saturation;
  }

  public double getNewBrightness() {
    return newBrightness;
  }

  public void setNewBrightness(double newBrightness) {
    this.newBrightness = newBrightness;
  }

  public double getOldBrightness() {
    return oldBrightness;
  }

  public void setOldBrightness(double oldBrightness) {
    this.oldBrightness = oldBrightness;
  }

  public boolean isColorsUpdated() {
    return colorsUpdated;
  }

  public void setColorsUpdated(boolean colorsUpdated) {
    this.colorsUpdated = colorsUpdated;
  }

  public byte[][] getCells() {
    return cells;
  }

  public void setCells(byte[][] cells) {
    this.cells = cells;
  }

  public Color[] getCellColors() {
    return cellColors;
  }

  public void setCellColors(Color[] cellColors) {
    this.cellColors = cellColors;
  }

  public Color getBackgroundColor() {
    return backgroundColor;
  }

  public void setBackgroundColor(Color backgroundColor) {
    this.backgroundColor = backgroundColor;
  }

  public Terrain getTerrain() {
    return terrain;
  }

  public WritableImage getBuffer() {
    return buffer;
  }

  public void setBuffer(WritableImage buffer) {
    this.buffer = buffer;
  }

  public PixelWriter getWriter() {
    return writer;
  }

  public void setWriter(PixelWriter writer) {
    this.writer = writer;
  }

  public boolean isBound() {
    return bound;
  }

  public void setBound(boolean bound) {
    this.bound = bound;
  }

  public void setTerrain(Terrain terrain) {
    this.terrain = terrain;
    int size = terrain.getSize();
    cells = new byte[size][size];
    buffer = new WritableImage(size, size);
    writer = buffer.getPixelWriter();
    colorsUpdated = false;

  }

  public static double getMaxHue() {
    return MAX_HUE;
  }

  public static double getMaxSaturation() {
    return MAX_SATURATION;
  }

  public static double getMaxBrightness() {
    return MAX_BRIGHTNESS;
  }

  public static double getDefaultHue() {
    return DEFAULT_HUE;
  }

  public static double getDefaultSaturation() {
    return DEFAULT_SATURATION;
  }

  public static double getDefaultNewBrightness() {
    return DEFAULT_NEW_BRIGHTNESS;
  }

  public static double getDefaultOldBrightness() {
    return DEFAULT_OLD_BRIGHTNESS;
  }

  public void draw() {
    if (buffer != null) {
      if (!colorsUpdated) {
        updateColors();
      }
      terrain.copyCells(cells);
      for( int rowIndex = 0; rowIndex < cells.length; rowIndex++){
        for (int colIndex = 0; colIndex < cells[rowIndex].length; colIndex++) {
          byte age = cells [rowIndex] [colIndex];
          writer.setColor(colIndex, rowIndex, (cells[rowIndex][colIndex] > 0)
              ? cellColors[age - 1]
              : backgroundColor);
        }
      }
      GraphicsContext context = getGraphicsContext2D();
      context.drawImage(buffer, 0,0, cellColors.length, cells.length, 0,0, getWidth(), getHeight());
    }
  }

  private void updateColors() {
    cellColors = new Color[Byte.MAX_VALUE];
    for (int i = 0; i < Byte.MAX_VALUE; i++) {
      cellColors[i] = Color.hsb(hue, saturation, oldBrightness + (newBrightness - oldBrightness) * (Byte.MAX_VALUE - i) / (Byte.MAX_VALUE));
    }

      backgroundColor = Color.hsb(hue, 0, 0);
    colorsUpdated = true;

  }

}
