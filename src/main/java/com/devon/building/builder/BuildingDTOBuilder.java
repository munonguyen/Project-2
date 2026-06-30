package com.devon.building.builder;

import com.devon.building.model.dto.BuildingDTO;
import java.util.List;

public class BuildingDTOBuilder {
    private Long id;
    private String name;
    private String street;
    private String ward;
    private Long numberOfBasement;
    private String districtId;
    private Long rentPrice;
    private Long floorArea;
    private String structure;
    private String direction;
    private String rentPriceDescription;
    private String serviceFee;
    private String carFee;
    private String overTimeFee;
    private Double brokerageFee;
    private String note;
    private byte[] image;
    private String uploadImage;
    private String managerName;
    private String managerPhoneNumber;
    private Long level;
    private String rentArea;
    private List<String> typeCode;

    public BuildingDTOBuilder id(Long id) { this.id = id; return this; }
    public BuildingDTOBuilder name(String name) { this.name = name; return this; }
    public BuildingDTOBuilder street(String street) { this.street = street; return this; }
    public BuildingDTOBuilder ward(String ward) { this.ward = ward; return this; }
    public BuildingDTOBuilder numberOfBasement(Long numberOfBasement) { this.numberOfBasement = numberOfBasement; return this; }
    public BuildingDTOBuilder districtId(String districtId) { this.districtId = districtId; return this; }
    public BuildingDTOBuilder rentPrice(Long rentPrice) { this.rentPrice = rentPrice; return this; }
    public BuildingDTOBuilder floorArea(Long floorArea) { this.floorArea = floorArea; return this; }
    public BuildingDTOBuilder structure(String structure) { this.structure = structure; return this; }
    public BuildingDTOBuilder direction(String direction) { this.direction = direction; return this; }
    public BuildingDTOBuilder rentPriceDescription(String rentPriceDescription) { this.rentPriceDescription = rentPriceDescription; return this; }
    public BuildingDTOBuilder serviceFee(String serviceFee) { this.serviceFee = serviceFee; return this; }
    public BuildingDTOBuilder carFee(String carFee) { this.carFee = carFee; return this; }
    public BuildingDTOBuilder overTimeFee(String overTimeFee) { this.overTimeFee = overTimeFee; return this; }
    public BuildingDTOBuilder brokerageFee(Double brokerageFee) { this.brokerageFee = brokerageFee; return this; }
    public BuildingDTOBuilder note(String note) { this.note = note; return this; }
    public BuildingDTOBuilder image(byte[] image) { this.image = image; return this; }
    public BuildingDTOBuilder uploadImage(String uploadImage) { this.uploadImage = uploadImage; return this; }
    public BuildingDTOBuilder managerName(String managerName) { this.managerName = managerName; return this; }
    public BuildingDTOBuilder managerPhoneNumber(String managerPhoneNumber) { this.managerPhoneNumber = managerPhoneNumber; return this; }
    public BuildingDTOBuilder level(Long level) { this.level = level; return this; }
    public BuildingDTOBuilder rentArea(String rentArea) { this.rentArea = rentArea; return this; }
    public BuildingDTOBuilder typeCode(List<String> typeCode) { this.typeCode = typeCode; return this; }

    public BuildingDTO build() {
        BuildingDTO dto = new BuildingDTO();
        dto.setId(this.id);
        dto.setName(this.name);
        dto.setStreet(this.street);
        dto.setWard(this.ward);
        dto.setNumberOfBasement(this.numberOfBasement);
        dto.setDistrictId(this.districtId);
        dto.setRentPrice(this.rentPrice);
        dto.setFloorArea(this.floorArea);
        dto.setStructure(this.structure);
        dto.setDirection(this.direction);
        dto.setRentPriceDescription(this.rentPriceDescription);
        dto.setServiceFee(this.serviceFee);
        dto.setCarFee(this.carFee);
        dto.setOverTimeFee(this.overTimeFee);
        dto.setBrokerageFee(this.brokerageFee);
        dto.setNote(this.note);
        dto.setImage(this.image);
        dto.setUploadImage(this.uploadImage);
        dto.setManagerName(this.managerName);
        dto.setManagerPhoneNumber(this.managerPhoneNumber);
        dto.setLevel(this.level);
        dto.setRentArea(this.rentArea);
        dto.setTypeCode(this.typeCode);
        return dto;
    }
}
