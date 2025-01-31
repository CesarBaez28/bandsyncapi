package com.bandsyncapi.bandsyncapi.services.impl;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.bandsyncapi.bandsyncapi.models.MusicalBandsModel;
import com.bandsyncapi.bandsyncapi.repositories.MusicalBandsRepository;
import com.bandsyncapi.bandsyncapi.services.MusicalBandsService;

@Service
public class MusicalBandsServiceImpl implements MusicalBandsService {

  private final MusicalBandsRepository musicalBandsRepository;

  public MusicalBandsServiceImpl(MusicalBandsRepository musicalBandsRepository) {
    this.musicalBandsRepository = musicalBandsRepository;
  }

  @Override
  public Optional<MusicalBandsModel> findById(UUID id) {
    return musicalBandsRepository.findById(id);
  }

  @Override
  public MusicalBandsModel save(MusicalBandsModel musicalBandsModel) {
    return musicalBandsRepository.save(musicalBandsModel);
  }
}
