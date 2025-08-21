package com.bandsyncapi.bandsyncapi.api.v1.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.bandsyncapi.bandsyncapi.api.v1.models.RepertoiresSongsKey;
import com.bandsyncapi.bandsyncapi.api.v1.models.RepertoiresSongsModel;
import jakarta.transaction.Transactional;

/*
 * This interface is a repository for the repertoires_songs table in the database.
 * Provides methods for performing CRUD operations on the repertoires_songs table.
 */
public interface RepertoiresSongsRepository extends JpaRepository<RepertoiresSongsModel, RepertoiresSongsKey> {

  /**
   * Deletes by a list of song id
   * 
   * @param songId - Song id list
   */
  @Modifying
  @Transactional
  @Query("""
      DELETE FROM RepertoiresSongsModel rs
      WHERE rs.song.id IN :songIds
      """)
  void deleteBySongIds(@Param("songIds") List<Integer> songIds);

  /**
   * Deletes by song id
   * 
   * @param songId - Song id 
   */
  @Modifying
  @Transactional
  @Query("""
      DELETE FROM RepertoiresSongsModel rs
      WHERE rs.song.id = :songId
      """)
  void deleteBySongId(@Param("songId") Integer songId);
}
