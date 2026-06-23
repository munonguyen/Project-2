package com.devon.building.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "rentarea")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RentArea {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(name="`value`")
    private Long value;

    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="buildingid")
    private Building building;

    @Column(name="createddate")
    private LocalDate createdDate;

    @Column(name="modifieddate")
    private LocalDate modifiedDate;

    @Column(name="createdby")
    private String createdBy;

    @Column(name="modifiedby")
    private String modifiedBy;
}
