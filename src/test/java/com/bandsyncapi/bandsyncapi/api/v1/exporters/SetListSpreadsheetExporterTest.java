package com.bandsyncapi.bandsyncapi.api.v1.exporters;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.UUID;

import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.junit.jupiter.api.Test;

import com.bandsyncapi.bandsyncapi.api.v1.dto.artists.ArtistsDto;
import com.bandsyncapi.bandsyncapi.api.v1.dto.setlists.SetDto;
import com.bandsyncapi.bandsyncapi.api.v1.dto.setlists.SetListDetailsDto;
import com.bandsyncapi.bandsyncapi.api.v1.dto.setlists.SetListSongsDto;
import com.bandsyncapi.bandsyncapi.api.v1.dto.setlists.SetListsDto;
import com.bandsyncapi.bandsyncapi.api.v1.dto.setlists.SetSongsDto;
import com.bandsyncapi.bandsyncapi.api.v1.dto.songs.SongsDto;
import com.bandsyncapi.bandsyncapi.api.v1.models.MusicalBandsModel;
import com.bandsyncapi.bandsyncapi.api.v1.models.RepertoiresModel;

class SetListSpreadsheetExporterTest {

  private final SetListSpreadsheetExporter exporter = new SetListSpreadsheetExporter();

  @Test
  void exportsSetListSetsAndSongDetails() throws Exception {
    var song = new SongsDto(1, "Song name", new ArtistsDto(1, "Artist name", true), null, "G", null, null);

    var musicalBand = MusicalBandsModel.builder().name("Two Baez").build();

    var repertoire = RepertoiresModel.builder()
        .musicalBand(musicalBand)
        .build();

    var setList = new SetListsDto(UUID.randomUUID(), repertoire, musicalBand, "Concert", "Live show", null, true);

    var set = SetListSongsDto.builder()
        .set(SetDto.builder().name("Opening set").build())
        .songs(List.of(SetSongsDto.builder().orderIndex(2).song(song)
            .notes("Cantante: Samuel Loveras\nCoros: Abigaíl")
            .build()))
        .build();

    var details = SetListDetailsDto.builder()
        .setList(setList)
        .sets(List.of(set))
        .build();

    byte[] file = exporter.export(details);

    try (var workbook = WorkbookFactory.create(new ByteArrayInputStream(file))) {
      var sheet = workbook.getSheetAt(0);

      assertEquals("Two Baez", sheet.getRow(0).getCell(0).getStringCellValue());
      assertEquals("Concert", sheet.getRow(2).getCell(0).getStringCellValue());
      assertEquals("Live show", sheet.getRow(3).getCell(0).getStringCellValue());
      assertEquals("Opening set", sheet.getRow(5).getCell(0).getStringCellValue());
      assertEquals("Orden", sheet.getRow(6).getCell(0).getStringCellValue());
      assertEquals("Canción", sheet.getRow(6).getCell(1).getStringCellValue());
      assertEquals(2, (int) sheet.getRow(7).getCell(0).getNumericCellValue());
      assertEquals("Song name", sheet.getRow(7).getCell(1).getStringCellValue());
      assertEquals("Artist name", sheet.getRow(7).getCell(2).getStringCellValue());
      assertEquals("G", sheet.getRow(7).getCell(3).getStringCellValue());
      assertEquals("Cantante: Samuel Loveras\nCoros: Abigaíl", sheet.getRow(7).getCell(4).getStringCellValue());

      assertTrue(sheet.getRow(7).getHeightInPoints() >= 38);
      assertEquals(4, sheet.getNumMergedRegions());

      assertEquals(IndexedColors.GREY_80_PERCENT.getIndex(), sheet.getRow(5).getCell(0).getCellStyle()
          .getFillForegroundColor());
      assertEquals(IndexedColors.WHITE.getIndex(), sheet.getRow(6).getCell(0).getCellStyle()
          .getFillForegroundColor());

      assertTrue(workbook.getFontAt(sheet.getRow(2).getCell(0).getCellStyle().getFontIndex()).getBold());
      assertEquals(HorizontalAlignment.LEFT, sheet.getRow(7).getCell(0).getCellStyle().getAlignment());
      assertTrue(sheet.getRow(7).getCell(4).getCellStyle().getWrapText());
      assertEquals(5, sheet.getPaneInformation().getHorizontalSplitPosition());
      assertTrue(!sheet.isDisplayGridlines());
    }
  }
}