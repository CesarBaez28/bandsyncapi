package com.bandsyncapi.bandsyncapi.api.v1.repositories;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.bandsyncapi.bandsyncapi.api.v1.models.SetsModel;


/**
 * Repository interface for managing SetsModel entities.
 */
public interface SetsRepository extends JpaRepository<SetsModel, UUID> {

  /**
   * Finds a list of SetsModel entities by the given setListId, ordered by
   * orderIndex in ascending order.
   * 
   * @param setListId the UUID of the set list to filter by
   * @return - a list of SetsModel entities associated with the specified
   *         setListId, ordered by orderIndex in ascending order
   */
  @Query("""
      SELECT s FROM SetsModel s
      JOIN FETCH s.setList sl
      WHERE sl.id = :setListId
      ORDER BY s.orderIndex ASC
      """)
  List<SetsModel> findBySetListIdOrderByOrderIndexAsc(@Param("setListId") UUID setListId);

  @Query("SELECT s.id FROM SetsModel s WHERE s.setList.id IN :setListIds")
  List<UUID> findIdsBySetListIds(@Param("setListIds") Set<UUID> setListIds);
}
