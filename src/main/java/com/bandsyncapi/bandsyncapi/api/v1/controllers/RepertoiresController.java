package com.bandsyncapi.bandsyncapi.api.v1.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bandsyncapi.bandsyncapi.api.v1.dto.repertoires.RepertoiresDto;
import com.bandsyncapi.bandsyncapi.api.v1.dto.repertoires.RepertoiresPostDto;
import com.bandsyncapi.bandsyncapi.api.v1.dto.repertoires.RepertoiresPutDto;
import com.bandsyncapi.bandsyncapi.api.v1.mappers.RepertoiresMapper;
import com.bandsyncapi.bandsyncapi.api.v1.models.RepertoiresModel;
import com.bandsyncapi.bandsyncapi.api.v1.services.RepertoiresService;
import com.bandsyncapi.bandsyncapi.api.v1.services.RepertoiresSongsService;
import com.bandsyncapi.bandsyncapi.response.ApiResponse;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

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
@RequestMapping(path = "api/v1/repertoires")
@Slf4j
public class RepertoiresController {

  private final RepertoiresService repertoiresService;

  private final RepertoiresMapper repertoiresMapper;

  private final RepertoiresSongsService repertoiresSongsService;

  /**
   * Constructor
   * 
   * @param repertoiresService      - Service with methods for performing CRUD
   *                                operations on the repertoires table.
   * @param repertoiresSongsService Service with methods for performing CRUD
   *                                operations on the repertoires_songs table.
   * @param repertoiresMapper       - Mapper to convert between RepertoiresModel
   *                                and RepertoiresDto.
   */
  public RepertoiresController(RepertoiresService repertoiresService, RepertoiresSongsService repertoiresSongsService,
      RepertoiresMapper repertoiresMapper) {
    this.repertoiresService = repertoiresService;
    this.repertoiresSongsService = repertoiresSongsService;
    this.repertoiresMapper = repertoiresMapper;
  }

  /**
   * Save a repertoire
   * 
   * @param repertoiresPostDto - request to save the repertoire
   * @return - An ApiResponse object
   */
  @PostMapping("/save")
  public ResponseEntity<ApiResponse<Void>> save(@Valid @RequestBody RepertoiresPostDto repertoiresPostDto) {
    RepertoiresModel repertoiresModel = repertoiresMapper.toModel(repertoiresPostDto);
    RepertoiresModel repertoiresModelSaved = repertoiresService.save(repertoiresModel);
    repertoiresSongsService.saveAll(repertoiresModelSaved, repertoiresPostDto.songs());

    log.info("Repertoire saved sucedfully with id: {}", repertoiresModelSaved.getId());

    return ResponseEntity.status(HttpStatus.CREATED)
        .body(new ApiResponse<>(true, "Repertorio guardado correctamente", null, null));
  }

  /**
   * finds repertoires by musical band id
   * 
   * @param id - musical band id
   * @return - A RepertoiresDto list
   */
  @GetMapping("/findByMusicalBandId/{id}")
  public ResponseEntity<ApiResponse<List<RepertoiresDto>>> findByMusicalBandId(@PathVariable UUID id) {
    List<RepertoiresModel> repertoiresModelList = repertoiresService.findByMusicalBandId(id);
    List<RepertoiresDto> repertoiresDtoList = repertoiresMapper.toDtoList(repertoiresModelList);

    log.info("Repertoires found by musical band id: {}", id);

    return ResponseEntity.status(HttpStatus.OK)
        .body(new ApiResponse<>(true, "Datos encontrados", repertoiresDtoList, null));
  }

  /**
   * Update repertoire
   * 
   * @param id                - repertoire id
   * @param repertoiresPutDto - Data to be updated
   * @return - An ApiResponse object
   */
  @PutMapping("/updateRepertoire/{id}")
  public ResponseEntity<ApiResponse<Void>> updateRepertoire(@PathVariable UUID id,
      @RequestBody RepertoiresPutDto repertoiresPutDto) {
    repertoiresService.updateRepertoire(id, repertoiresPutDto);

    log.info("Repertoire updated successfully with id: {}", id);

    return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(true, "Repertorio actualizado.", null, null));
  }

}
