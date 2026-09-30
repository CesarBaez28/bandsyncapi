package com.bandsyncapi.bandsyncapi.api.v1.models;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "sets")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SetsModel {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @ManyToOne
  @JoinColumn(name = "set_list_id", nullable = false)
  private SetListsModel setList;

  @Column(name = "name", nullable = false, length = 100)
  private String name;

  @Column(name = "order_index", nullable = false)
  private Integer orderIndex;

  @Column(name = "status", nullable = false, columnDefinition = "BIT DEFAULT 1")
  @Builder.Default
  private Boolean status = true;
}
