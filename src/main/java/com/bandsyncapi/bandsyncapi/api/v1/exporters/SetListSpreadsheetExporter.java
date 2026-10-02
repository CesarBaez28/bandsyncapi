package com.bandsyncapi.bandsyncapi.api.v1.exporters;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import com.bandsyncapi.bandsyncapi.api.v1.dto.setlists.SetListDetailsDto;
import com.bandsyncapi.bandsyncapi.api.v1.dto.setlists.SetListSongsDto;
import com.bandsyncapi.bandsyncapi.api.v1.dto.setlists.SetSongsDto;
import com.bandsyncapi.bandsyncapi.api.v1.dto.songs.SongsDto;

import lombok.extern.slf4j.Slf4j;

/**
 * This class is used to export set lists content
 * 
 */
@Component
@Slf4j
public class SetListSpreadsheetExporter {

  private static final String[] SONG_HEADERS = { "Orden", "Canción", "Artista", "Tonalidad", "Notas" };
  private static final int NOTES_COLUMN_WIDTH = 46;
  private static final int NOTES_CHARACTERS_PER_LINE = 42;

  /**
   * Generate an excel with the information of a set list
   * 
   * @param setListDetails - set list information
   * @return
   */
  public byte[] export(SetListDetailsDto setListDetails) {
    log.info("Generating excel document for set list with id: {}", setListDetails.setList().id());

    try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
      Sheet sheet = workbook.createSheet("Setlist");

      SpreadsheetStyles styles = createStyles(workbook);

      writeSetListMetadata(sheet, setListDetails, styles);

      int lastRowIndex = writeSets(sheet, setListDetails.sets(), styles);

      configureColumns(sheet);

      configureSheet(sheet, workbook, lastRowIndex);

      workbook.write(outputStream);

      return outputStream.toByteArray();
    } catch (IOException exception) {
      throw new IllegalStateException("Unable to generate set list spreadsheet", exception);
    }
  }

  private void writeSetListMetadata(Sheet sheet, SetListDetailsDto setListDetails, SpreadsheetStyles styles) {
    Row bandRow = sheet.createRow(0);
    bandRow.setHeightInPoints(38);
    writeMergedTextRow(sheet, bandRow, 0, 4, bandName(setListDetails), styles.bandName());

    sheet.createRow(1).setHeightInPoints(14);

    Row nameRow = sheet.createRow(2);
    nameRow.setHeightInPoints(36);
    writeMergedTextRow(sheet, nameRow, 0, 4, setListDetails.setList().name(), styles.title());

    Row descriptionRow = sheet.createRow(3);
    descriptionRow.setHeightInPoints(28);
    writeMergedTextRow(sheet, descriptionRow, 0, 4, setListDetails.setList().description(), styles.description());
  }

  private int writeSets(Sheet sheet, List<SetListSongsDto> sets, SpreadsheetStyles styles) {
    int rowIndex = 5;

    for (SetListSongsDto setDetails : sets) {
      rowIndex = writeSet(sheet, setDetails, styles, rowIndex) + 1;
    }

    return Math.max(1, rowIndex - 1);
  }

  private int writeSet(Sheet sheet, SetListSongsDto setDetails, SpreadsheetStyles styles, int rowIndex) {
    Row setNameRow = sheet.createRow(rowIndex++);
    setNameRow.setHeightInPoints(24);
    writeMergedTextRow(sheet, setNameRow, 0, 4, setDetails.set().name(), styles.setTitle());

    Row headerRow = sheet.createRow(rowIndex++);
    headerRow.setHeightInPoints(22);

    for (int columnIndex = 0; columnIndex < SONG_HEADERS.length; columnIndex++) {
      writeTextCell(headerRow, columnIndex, SONG_HEADERS[columnIndex], styles.tableHeader());
    }

    for (SetSongsDto setSong : setDetails.songs()) {
      Row songRow = sheet.createRow(rowIndex++);
      songRow.setHeightInPoints(songRowHeight(setSong));
      writeSong(songRow, setSong, styles);
    }

    return rowIndex;
  }

  private void writeSong(Row row, SetSongsDto setSong, SpreadsheetStyles styles) {
    SongsDto song = setSong.song();

    writeNumberCell(row, 0, setSong.orderIndex(), styles.order());
    writeTextCell(row, 1, song == null ? null : song.name(), styles.song());
    writeTextCell(row, 2, artistName(song), styles.song());
    writeTextCell(row, 3, song == null ? null : song.tonality(), styles.tonality());
    writeTextCell(row, 4, setSong.notes(), styles.notes());
  }

  private String bandName(SetListDetailsDto setListDetails) {
    var musicalBand = setListDetails.setList().musicalBand();

    if (musicalBand == null) {
      return "";
    }

    return musicalBand.getName();
  }

  private String artistName(SongsDto song) {

    if (song == null || song.artist() == null) {
      return null;
    }

    return song.artist().name();
  }

  private void configureColumns(Sheet sheet) {
    sheet.setColumnWidth(0, 11 * 256);
    sheet.setColumnWidth(1, 34 * 256);
    sheet.setColumnWidth(2, 28 * 256);
    sheet.setColumnWidth(3, 16 * 256);
    sheet.setColumnWidth(4, NOTES_COLUMN_WIDTH * 256);
  }

  private void configureSheet(Sheet sheet, Workbook workbook, int lastRowIndex) {
    sheet.setDisplayGridlines(false);
    sheet.createFreezePane(0, 5);
    sheet.setFitToPage(true);
    sheet.getPrintSetup().setLandscape(true);
    sheet.getPrintSetup().setFitWidth((short) 1);
    sheet.getPrintSetup().setFitHeight((short) 0);
    workbook.setPrintArea(0, 0, SONG_HEADERS.length - 1, 0, lastRowIndex);
  }

  private float songRowHeight(SetSongsDto setSong) {
    String notes = setSong.notes();

    if (notes == null || notes.isBlank()) {
      return 24;
    }

    int estimatedLines = 0;
    for (String line : notes.split("\\R", -1)) {
      estimatedLines += Math.max(1, (int) Math.ceil(line.length() / (double) NOTES_CHARACTERS_PER_LINE));
    }

    return Math.max(24, estimatedLines * 15 + 8);
  }

  private SpreadsheetStyles createStyles(Workbook workbook) {

    CellStyle bandName = createStyle(workbook, IndexedColors.WHITE, IndexedColors.BLACK, 20, true,
        HorizontalAlignment.LEFT, VerticalAlignment.CENTER);

    CellStyle title = createStyle(workbook, IndexedColors.WHITE, IndexedColors.BLACK, 22, true,
        HorizontalAlignment.LEFT, VerticalAlignment.CENTER);

    CellStyle description = createStyle(workbook, IndexedColors.WHITE, IndexedColors.GREY_50_PERCENT, 12, false,
        HorizontalAlignment.LEFT, VerticalAlignment.CENTER);

    CellStyle setTitle = createStyle(workbook, IndexedColors.GREY_80_PERCENT, IndexedColors.WHITE, 13, true,
        HorizontalAlignment.LEFT, VerticalAlignment.CENTER);

    CellStyle tableHeader = createStyle(workbook, IndexedColors.WHITE, IndexedColors.BLACK, 11, true,
        HorizontalAlignment.LEFT, VerticalAlignment.CENTER);

    tableHeader.setBorderBottom(BorderStyle.MEDIUM);

    CellStyle song = createStyle(workbook, IndexedColors.WHITE, IndexedColors.BLACK, 11, false,
        HorizontalAlignment.LEFT, VerticalAlignment.TOP);

    CellStyle order = createStyle(workbook, IndexedColors.WHITE, IndexedColors.BLACK, 11, false,
        HorizontalAlignment.LEFT, VerticalAlignment.TOP);

    CellStyle tonality = createStyle(workbook, IndexedColors.WHITE, IndexedColors.BLACK, 11, false,
        HorizontalAlignment.LEFT, VerticalAlignment.TOP);

    CellStyle notes = createStyle(workbook, IndexedColors.WHITE, IndexedColors.BLACK, 11, false,
        HorizontalAlignment.LEFT, VerticalAlignment.TOP);

    return new SpreadsheetStyles(bandName, title, description, setTitle, tableHeader, song, order, tonality, notes);
  }

  private CellStyle createStyle(Workbook workbook, IndexedColors fillColor, IndexedColors fontColor, int fontSize,
      boolean bold, HorizontalAlignment horizontalAlignment, VerticalAlignment verticalAlignment) {

    Font font = workbook.createFont();
    font.setFontName("Arial");
    font.setBold(bold);
    font.setFontHeightInPoints((short) fontSize);
    font.setColor(fontColor.getIndex());

    CellStyle style = workbook.createCellStyle();
    style.setFont(font);
    style.setFillForegroundColor(fillColor.getIndex());
    style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
    style.setAlignment(horizontalAlignment);
    style.setVerticalAlignment(verticalAlignment);
    style.setWrapText(true);
    style.setBorderBottom(org.apache.poi.ss.usermodel.BorderStyle.THIN);
    style.setBottomBorderColor(IndexedColors.GREY_25_PERCENT.getIndex());

    return style;
  }

  private void writeMergedTextRow(Sheet sheet, Row row, int firstColumn, int lastColumn, String value,
      CellStyle style) {
    for (int columnIndex = firstColumn; columnIndex <= lastColumn; columnIndex++) {

      Cell cell = row.createCell(columnIndex);

      cell.setCellStyle(style);

      if (columnIndex == firstColumn) {
        cell.setCellValue(value == null ? "" : value);
      }
    }

    sheet.addMergedRegion(new CellRangeAddress(row.getRowNum(), row.getRowNum(), firstColumn, lastColumn));
  }

  private void writeTextCell(Row row, int columnIndex, String value, CellStyle style) {
    Cell cell = row.createCell(columnIndex);
   
    cell.setCellValue(value == null ? "" : value);

    if (style != null) {
      cell.setCellStyle(style);
    }
  }

  private void writeNumberCell(Row row, int columnIndex, Integer value, CellStyle style) {
    Cell cell = row.createCell(columnIndex);

    if (value != null) {
      cell.setCellValue(value);
    } else {
      cell.setBlank();
    }
    
    cell.setCellStyle(style);
  }

  private record SpreadsheetStyles(
      CellStyle bandName,
      CellStyle title,
      CellStyle description,
      CellStyle setTitle,
      CellStyle tableHeader,
      CellStyle song,
      CellStyle order,
      CellStyle tonality,
      CellStyle notes) {
  }
}