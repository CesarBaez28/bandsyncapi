package com.bandsyncapi.bandsyncapi.api.v1.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.bandsyncapi.bandsyncapi.api.v1.models.ArtistsModel;
import com.bandsyncapi.bandsyncapi.api.v1.models.MusicalGenresModel;
import com.bandsyncapi.bandsyncapi.api.v1.models.SongsModel;

import jakarta.transaction.Transactional;

/*
 * This interface is a repository for the songs table in the database.
 * Provides methods for performing CRUD operations on the songs table.
 */
public interface SongsRepository extends JpaRepository<SongsModel, Integer> {

  /**
   * finds songs by musical band id
   * 
   * @param musicalBandId - musical band id
   * @return - SongsModel list
   */
  @Query("""
      SELECT s FROM SongsModel s
      JOIN s.musicalBand mb
      WHERE mb.id = :musicalBandId
      """)
  List<SongsModel> findByMusicalBandId(@Param("musicalBandId") UUID musicalBandId);

  /**
   * finds song by artist id
   * 
   * @param artistId - artist id
   * @return - A SongsModel List
   */
  @Query("""
      SELECT s FROM SongsModel s
      JOIN s.artist a
      WHERE a.id = :artistId
            """)
  List<SongsModel> findByArtistId(@Param("artistId") Integer artistId);

  /**
   * finds songs by genre id
   * 
   * @param genreId - Musical genre id
   * @return - A SongsModel List
   */
  @Query("""
      SELECT s FROM SongsModel s
      JOIN s.genre g
      WHERE g.id = :genreId
            """)
  List<SongsModel> findByGenreId(@Param("genreId") Integer genreId);

  /**
   * Update song info
   * 
   * @param id         - song id
   * @param artist     - Artist id
   * @param genre      - genre id
   * @param tonality   - tonality song
   * @param link       - link to share song
   * @param sheetMusic - sheetMusic
   * @return - number of rows updated
   */
  @Transactional
  @Modifying
  @Query("""
      UPDATE SongsModel s
      SET s.name = :name, s.artist = :artist, s.genre = :genre, s.tonality = :tonality, s.link = :link, s.sheetMusic = :sheetMusic
      WHERE s.id = :id
      """)
  int updateSong(@Param("id") Integer id, @Param("name") String name, @Param("artist") ArtistsModel artist,
      @Param("genre") MusicalGenresModel genre,
      @Param("tonality") String tonality, @Param("link") String link, @Param("sheetMusic") String sheetMusic);

  /**
   * Deletes songs by artist id
   * 
   * @param artistId - Artist id
   */
  @Transactional
  @Modifying
  @Query("""
      DELETE FROM SongsModel s
      WHERE s.artist.id = :artistId
            """)
  void deleteByArtistId(@Param("artistId") Integer artistId);

  /**
   * Deletes songs by genre id
   * 
   * @param genreId - Genre id
   */
  @Transactional
  @Modifying
  @Query("""
      DELETE FROM SongsModel s
      WHERE s.genre.id = :genreId
            """)
  void deleteByGenreId(@Param("genreId") Integer genreId);
}
