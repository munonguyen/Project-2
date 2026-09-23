package com.devon.building.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "building")
public class BuildingEntity extends BaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = -1000119078147252957L;

    @Column(name = "name", length = 255, nullable = false)
    String name;

    @Column(name = "street", length = 255, nullable = false)
    String street;

    @Column(name = "ward", length = 255, nullable = false)
    String ward;

    @Lob
    @Column(name = "image", length = Integer.MAX_VALUE, nullable = true)
    byte[] image;


    @Column(name = "district", length = 255, nullable = false)
    String district;

    @Column(name = "structure")
    String structure;

    @Column(name = "numberofbasement")
    Integer numberOfBasement;

    @Column(name = "floorarea")
    Long floorArea;

    @Column(name = "direction")
    String direction;

    @Column(name = "level")
    String level;

    @Column(name = "rentprice", nullable = false)
    double price;


    @Column(name = "rentpricedescription")
    String rentPriceDescription;

    @Column(name = "servicefee" )
    String serviceFee;

    @Column(name = "carfee")
    String carFee;

    @Column(name = "motofee")
    String motoFee;

    @Column(name = "overtimefee")
    String overtimeFee;

    @Column(name = "waterfee")
    String waterFee;

    @Column(name = "electricityfee")
    String electricityFee;

    @Column(name = "deposit")
    String deposit;

    @Column(name = "payment")
    String payment;

    @Column(name = "renttime")
    String rentTime;

    @Column(name = "decorationtime")
    String decorationTime;

    @Column(name = "brokeragefee")
    Double brokerageFee;

    @Column(name = "type")
    private String rentType;

    @Column(name = "note")
    String note;

    @Column(name = "linkofbuilding")
    String linkOfBuilding;

    @Column(name = "map")
    String map;

    @Column(name = "managername")
    String managerName;

    @Column(name = "managerphone")
    String managerPhone;

    @OneToMany(mappedBy = "building", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RentAreaEntity> rentArea = new ArrayList<>();


    @ManyToMany
    @JoinTable(
            name = "assignmentbuilding",
            joinColumns = @JoinColumn(name = "buildingid"),
            inverseJoinColumns = @JoinColumn(name = "staffid")
    )
    private List<User> user = new ArrayList<>();

}
