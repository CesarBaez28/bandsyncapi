package com.bandsyncapi.bandsyncapi.api.v1.services;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import com.bandsyncapi.bandsyncapi.api.v1.models.ArtistsModel;
import com.bandsyncapi.bandsyncapi.api.v1.models.MusicalGenresModel;
import com.bandsyncapi.bandsyncapi.api.v1.models.SongsModel;

/**
 * Interface that defines methods for SongsModel
 */
public interface SongsService {

  /**
   * Save a song
   * 
   * @param song - new song
   * @param file - sheetMusic or something else
   * @return - SongsModel @see SongsModel
   */
  SongsModel save (SongsModel song, MultipartFile file) throws IOException;

  /**
   * finds songs by musical band id
   * 
   * @param musicalBandId - musical band id
   * @return - SongsModel List @see SongsModel
   */
  List<SongsModel> findByMusicalBandId (UUID musicalBandId);

  /**
   * Finds songs by musical band id and name, artist name, genre name and tonality
   * 
   * @param musicalBandId - Musical Band id
   * @param term - Search term
   * @param page - Page number
   * @param size - Page size
   * @return - A List of SongsModel
   */
  Page<SongsModel> find(UUID musicalBandId, String term, int page, int size);

  /**
   * update song info
   * 
   * @param id - song id
   * @param name - song name
   * @param artistId - artist id
   * @param genreId - genre id
   * @param tonality - Song tonality
   * @param link - Link to share song
   * @param sheetMusic - SheetMusic
   */
  void updateSong (Integer id, String name, ArtistsModel artist, MusicalGenresModel genre, String tonality, String link, String sheetMusic);

  /**
   * Deletes a song by artist id
   * 
   * @param artistId - artist id
   */
  void deleteByArtistId (Integer artistId);

  /**
   * Deletes a song by genre id
   * 
   * @param genreId
   */
  void deleteByGenreId (Integer genreId);
}
