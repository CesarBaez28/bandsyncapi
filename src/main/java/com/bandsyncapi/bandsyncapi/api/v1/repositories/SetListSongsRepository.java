package com.bandsyncapi.bandsyncapi.api.v1.repositories;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.bandsyncapi.bandsyncapi.api.v1.models.SetListSongsModel;

/**
 * Repository interface for managing SetListSongsModel entities.
 */
public interface SetListSongsRepository extends JpaRepository<SetListSongsModel, UUID> {

  /**
   * Finds a list of SetListSongsModel entities by the given setIds, ordered by
   * orderIndex in ascending order.
   * 
   * @param setIds the set of UUIDs of the sets to filter by
   * @return - a list of SetListSongsModel entities associated with the specified
   *         setIds, ordered by orderIndex in ascending order
   */
  @Query("""
      SELECT sls FROM SetListSongsModel sls
      JOIN FETCH sls.set s
      JOIN FETCH sls.song sg
      WHERE s.id IN :setIds
      ORDER BY s.orderIndex ASC, sls.orderIndex ASC
      """)
  List<SetListSongsModel> findBySetIdsOrderByOrderIndexAsc(@Param("setIds") Set<UUID> setIds);

  @Query("""
      SELECT sls FROM SetListSongsModel sls
      JOIN FETCH sls.set s
      JOIN FETCH sls.song sg
      WHERE s.id = :setId
      ORDER BY sls.orderIndex ASC
      """)
  List<SetListSongsModel> findBySetId(@Param("setId") UUID setId);

  @Modifying(flushAutomatically = true, clearAutomatically = true)
  @Query(value = """
      DELETE FROM set_list_songs
      WHERE set_id IN :setIds
      """, nativeQuery = true)
  void deleteBySetIds(@Param("setIds") Set<UUID> setIds);
}
