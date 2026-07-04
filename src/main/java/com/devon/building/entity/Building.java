package com.devon.building.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "building")
public class Building extends BaseEntity {

    @Serial
    private static final long serialVersionUID = -1000119078147252957L;

    @Column(name = "name", length = 255, nullable = false)
    private String name;

    @Column(name = "street", length = 255)
    private String street;

    @Column(name = "ward", length = 255)
    private String ward;

    @Column(name = "district", length = 255, nullable = false)
    private String district;

    @Column(name = "structure", length = 255)
    private String structure;

    @Column(name = "numberofbasement")
    private Integer numberOfBasement;

    @Column(name = "floorarea")
    private Long floorArea;

    @Column(name = "direction", length = 255)
    private String direction;

    @Column(name = "level", length = 255)
    private String level;

    @Column(name = "rentprice", nullable = false)
    private double price;

    @Column(name = "rentpricedescription", columnDefinition = "text")
    private String rentPriceDescription;

    @Column(name = "servicefee", length = 255)
    private String serviceFee;

    @Column(name = "carfee", length = 255)
    private String carFee;

    @Column(name = "overtimefee", length = 255)
    private String overTimeFee;

    @Column(name = "brokeragefee")
    private BigDecimal brokerageFee;

    @Column(name = "type", length = 255, nullable = false)
    private String type;

    @Column(name = "note", length = 255)
    private String note;

    @Column(name = "motofee", length = 255)
    private String motoFee;

    @Column(name = "waterfee", length = 255)
    private String waterFee;

    @Column(name = "electricityfee", length = 255)
    private String electricityFee;

    @Column(name = "deposit", length = 255)
    private String deposit;

    @Column(name = "payment", length = 255)
    private String payment;

    @Column(name = "renttime", length = 255)
    private String rentTime;

    @Column(name = "decorationtime", length = 255)
    private String decorationTime;

    @Column(name = "linkofbuilding", length = 255)
    private String linkOfBuilding;

    @Column(name = "map", length = 255)
    private String map;

    @Column(name = "managername", length = 255)
    private String managerName;

    @Column(name = "managerphone", length = 255)
    private String managerPhoneNumber;

    @Lob
    @Column(name = "image", length = Integer.MAX_VALUE)
    private byte[] image;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "assignmentbuilding",
            joinColumns = @JoinColumn(name = "buildingid", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "staffid", nullable = false)
    )
    private List<User> staffs = new ArrayList<>();

    @OneToMany(
            mappedBy = "building",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<RentArea> rentAreas = new ArrayList<>();

    public List<User> getStaffs() {
        return Collections.unmodifiableList(staffs);
    }

    public List<RentArea> getRentAreas() {
        return Collections.unmodifiableList(rentAreas);
    }

    public void addStaff(User staff) {
        Objects.requireNonNull(staff, "staff must not be null");
        linkStaff(staff);
    }

    public void removeStaff(User staff) {
        if (staff == null) {
            return;
        }
        unlinkStaff(staff);
    }

    public void clearStaffs() {
        new ArrayList<>(staffs).forEach(this::removeStaff);
    }

    public void replaceStaffs(Collection<User> staffs) {
        clearStaffs();
        staffs.forEach(this::addStaff);
    }

    public void addRentArea(RentArea rentArea) {
        Objects.requireNonNull(rentArea, "rentArea must not be null");
        linkRentArea(rentArea);
    }

    public void removeRentArea(RentArea rentArea) {
        if (rentArea == null) {
            return;
        }
        unlinkRentArea(rentArea);
    }

    public void clearRentAreas() {
        new ArrayList<>(rentAreas).forEach(this::removeRentArea);
    }

    public void replaceRentAreas(Collection<RentArea> rentAreas) {
        clearRentAreas();
        rentAreas.forEach(this::addRentArea);
    }

    private void linkStaff(User staff) {
        if (staffs.contains(staff)) {
            return;
        }
        staffs.add(staff);
        if (!staff.getBuildings().contains(this)) {
            staff.getBuildings().add(this);
        }
    }

    private void unlinkStaff(User staff) {
        if (!staffs.remove(staff)) {
            return;
        }
        staff.getBuildings().remove(this);
    }

    private void linkRentArea(RentArea rentArea) {
        if (rentAreas.contains(rentArea)) {
            return;
        }
        rentAreas.add(rentArea);
        rentArea.setBuilding(this);
    }

    private void unlinkRentArea(RentArea rentArea) {
        if (!rentAreas.remove(rentArea)) {
            return;
        }
        rentArea.setBuilding(null);
    }

}
