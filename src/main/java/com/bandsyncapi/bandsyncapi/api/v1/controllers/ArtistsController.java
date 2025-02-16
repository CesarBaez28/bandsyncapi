package com.bandsyncapi.bandsyncapi.api.v1.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bandsyncapi.bandsyncapi.api.v1.dto.artists.ArtistsDto;
import com.bandsyncapi.bandsyncapi.api.v1.dto.artists.ArtistsPostDto;
import com.bandsyncapi.bandsyncapi.api.v1.dto.artists.ArtistsPutDto;
import com.bandsyncapi.bandsyncapi.api.v1.mappers.ArtistsMapper;
import com.bandsyncapi.bandsyncapi.api.v1.models.ArtistsModel;
import com.bandsyncapi.bandsyncapi.api.v1.services.ArtistsService;
import com.bandsyncapi.bandsyncapi.response.ApiResponse;

import jakarta.validation.Valid;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

/**
 * This is the controller to handle requests for the artists table.
 */
@RestController
@RequestMapping(path = "api/v1/artists")
public class ArtistsController {

  private final ArtistsService artistsService;

  private final ArtistsMapper artistsMapper;

  /**
   * Constructor
   * 
   * @param artistsService - Service with methods for performing CRUD
   *                       operations on the artists table.
   * @param artistsMapper  - Mapper to convert between ArtistsModel and
   *                       ArtistsDto.
   */
  public ArtistsController(ArtistsService artistsService, ArtistsMapper artistsMapper) {
    this.artistsService = artistsService;
    this.artistsMapper = artistsMapper;
  }

  /**
   * Save a new artist
   * 
   * @param artistsPostDto - Request to save the new artist
   * @return - An ApiResponse object with the new artist
   */
  @PostMapping("/save")
  public ResponseEntity<ApiResponse<ArtistsDto>> save(@Valid @RequestBody ArtistsPostDto artistsPostDto) {
    ArtistsModel artistsModel = artistsMapper.toModel(artistsPostDto);
    ArtistsModel artistsModelSaved = artistsService.save(artistsModel);
    ArtistsDto artistsDto = artistsMapper.toDto(artistsModelSaved);

    return ResponseEntity.status(HttpStatus.CREATED)
        .body(new ApiResponse<>(true, "Artista registrado correctamente", artistsDto, null));
  }

  /**
   * finds artists by musical band id
   * 
   * @param id - musical band id
   * @return An ApiReponse Object with a List of artists
   */
  @GetMapping("/findByMusicalBandId/{id}")
  public ResponseEntity<ApiResponse<List<ArtistsDto>>> findByMusicalRoleId(@PathVariable UUID id) {
    List<ArtistsModel> artistsModelList = artistsService.findByMusicalBandId(id);
    List<ArtistsDto> artistsDtoList = artistsMapper.toDtoList(artistsModelList);

    return ResponseEntity.status(HttpStatus.OK)
        .body(new ApiResponse<>(true, "Datos encontrados", artistsDtoList, null));
  }

  /**
   * Update artist name
   * 
   * @param id            - Artist id
   * @param artistsPutDto - the request body to update the artist
   * @return - An ApiReponse object indicating that the artist name was
   *         updated
   */
  @PutMapping("/updateArtistName/{id}")
  public ResponseEntity<ApiResponse<Void>> updateArtistName(@PathVariable Integer id,
      @RequestBody ArtistsPutDto artistsPutDto) {
    artistsService.updateArtist(id, artistsPutDto.name());

    return ResponseEntity.status(HttpStatus.OK)
        .body(new ApiResponse<>(true, "Nombre de artista actualizado correctamente.", null, null));
  }

  /**
   * Deletes an Artist by id
   * 
   * @param id - Artist id
   * @return An ApiResponse object indicating that the musical genre was deleted
   */
  @DeleteMapping("/delete/{id}")
  public ResponseEntity<ApiResponse<Void>> deleteById(@PathVariable Integer id) {
    artistsService.deleteById(id);

    return ResponseEntity.status(HttpStatus.OK)
        .body(new ApiResponse<>(true, "Artista eliminado correctamente", null, null));
  }
}
