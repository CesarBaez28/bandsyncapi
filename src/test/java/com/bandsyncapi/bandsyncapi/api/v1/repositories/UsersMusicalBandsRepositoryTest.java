package com.bandsyncapi.bandsyncapi.api.v1.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.bandsyncapi.bandsyncapi.api.v1.models.MusicalBandsModel;
import com.bandsyncapi.bandsyncapi.api.v1.models.UsersModel;
import com.bandsyncapi.bandsyncapi.api.v1.models.UsersMusicalBandsModel;

@DataJpaTest
class UsersMusicalBandsRepositoryTest {

  @Autowired
  private UsersMusicalBandsRepository usersMusicalBandsRepository;

  @Autowired
  private UsersRepository usersRepository;

  @Autowired
  private MusicalBandsRepository musicalBandsRepository;

  @Test
  void testFindByUser() {

    // Given
    var user = UsersModel.builder()
        .username("testUsername")
        .password("testPassword123#")
        .email("Test email")
        .firstName("test first name")
        .lastName("test last name")
        .phone("8094232343")
        .photo("http://Test")
        .status(true)
        .build();

    usersRepository.save(user);

    var musicalBand = MusicalBandsModel.builder()
        .name("Test musical band")
        .email("test@gmail.com")
        .hyphenatedName("test-musical-band")
        .address("Test address")
        .phone("8094341234")
        .logo("http://test.com")
        .status(true)
        .build();

    var musicalBand2 = MusicalBandsModel.builder()
        .name("Test musical band 2")
        .email("test2@gmail.com")
        .hyphenatedName("test-musical-band-2")
        .address("Test address 2")
        .phone("8094563452")
        .logo("http://test.com")
        .status(true)
        .build();
        
    musicalBandsRepository.save(musicalBand);
    musicalBandsRepository.save(musicalBand2);

    var usersMusicalBandsModel = new UsersMusicalBandsModel(user, musicalBand, true);
    var usersMusicalBandsModel2 = new UsersMusicalBandsModel(user, musicalBand2, true);

    List<UsersMusicalBandsModel> list = List.of(usersMusicalBandsModel, usersMusicalBandsModel2);
    usersMusicalBandsRepository.saveAll(list);

    // When
    List<UsersMusicalBandsModel> result = usersMusicalBandsRepository.findByUser(user);

    // Then
    assertFalse(result.isEmpty());
    assertEquals(list.get(0).getMusicalBand().getName(), result.get(0).getMusicalBand().getName());
    assertEquals(list.get(1).getMusicalBand().getName(), result.get(1).getMusicalBand().getName());
  }
}
