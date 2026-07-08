package com.devon.building.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "rentarea")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RentArea extends BaseEntity {

  @Column(name = "`value`")
  private Long value;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "buildingid")
  private Building building;
}
