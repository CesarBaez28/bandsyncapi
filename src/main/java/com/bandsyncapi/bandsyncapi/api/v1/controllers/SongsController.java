package com.bandsyncapi.bandsyncapi.api.v1.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bandsyncapi.bandsyncapi.api.v1.dto.songs.SongsDto;
import com.bandsyncapi.bandsyncapi.api.v1.dto.songs.SongsPostDto;
import com.bandsyncapi.bandsyncapi.api.v1.dto.songs.SongsPutDto;
import com.bandsyncapi.bandsyncapi.api.v1.mappers.SongsMapper;
import com.bandsyncapi.bandsyncapi.api.v1.models.SongsModel;
import com.bandsyncapi.bandsyncapi.api.v1.services.SongsService;
import com.bandsyncapi.bandsyncapi.response.ApiResponse;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping(path = "api/v1/songs")
public class SongsController {

  private final SongsService songsService;

  private final SongsMapper songsMapper;

  /**
   * Constructor of the class
   * 
   * @param songsService - Songs service with all business logic
   * @param songsMapper  - Mapper to convert between SongsModel and
   *                     SongsDto.
   */
  public SongsController(SongsService songsService, SongsMapper songsMapper) {
    this.songsService = songsService;
    this.songsMapper = songsMapper;
  }

  /**
   * save a song
   * 
   * @param songsPostDto
   * @return - SongsDto @see SongsDto
   */
  @PostMapping("/save")
  public ResponseEntity<ApiResponse<SongsDto>> save(@RequestBody SongsPostDto songsPostDto) {
    SongsModel songsModel = songsMapper.toModel(songsPostDto);
    SongsModel songsModelSaved = songsService.save(songsModel);
    SongsDto songsDtoResponse = songsMapper.toDto(songsModelSaved);

    return ResponseEntity.status(HttpStatus.CREATED)
        .body(new ApiResponse<>(true, "Datos guardados", songsDtoResponse, null));
  }

  /**
   * finds songs by musical band id
   * 
   * @param id - musical band id
   * @return - A SongsModel List
   */
  @GetMapping("/findByMusicalBandId/{id}")
  public ResponseEntity<ApiResponse<List<SongsDto>>> findByMusicalBandId(@PathVariable UUID id) {
    List<SongsModel> songsModelList = songsService.findByMusicalBandId(id);
    List<SongsDto> songsDtoResponse = songsMapper.toDtoList(songsModelList);

    return ResponseEntity.status(HttpStatus.OK)
        .body(new ApiResponse<>(true, "Datos encontrados", songsDtoResponse, null));
  }

  /**
   * Update song info
   * 
   * @param id          - song id
   * @param songsPutDto - Song info to be updated
   * @return
   */
  @PutMapping("updateSong/{id}")
  public ResponseEntity<ApiResponse<Void>> update(@PathVariable Integer id, @RequestBody SongsPutDto songsPutDto) {
    songsService.updateSong(id, songsPutDto.name(), songsPutDto.artist(), songsPutDto.genre(),
        songsPutDto.tonality(), songsPutDto.link(), songsPutDto.sheetMusic());

    return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(true, "Datos actualizados", null, null));
  }
}
