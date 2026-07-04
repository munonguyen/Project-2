package com.devon.building.builder;

import com.devon.building.model.dto.response.BuildingSearchResponse;

public class BuildingSearchResponseBuilder {
    private Long id;
    private String name;
    private String address;
    private String managerName;
    private String managerPhoneNumber;
    private Long floorArea;
    private String rentArea;
    private Long rentPrice;
    private String serviceFee;
    private Double brokerageFee;
    private String numberOfBasement;

    public BuildingSearchResponseBuilder id(Long id) { this.id = id; return this; }
    public BuildingSearchResponseBuilder name(String name) { this.name = name; return this; }
    public BuildingSearchResponseBuilder address(String address) { this.address = address; return this; }
    public BuildingSearchResponseBuilder managerName(String managerName) { this.managerName = managerName; return this; }
    public BuildingSearchResponseBuilder managerPhoneNumber(String managerPhoneNumber) { this.managerPhoneNumber = managerPhoneNumber; return this; }
    public BuildingSearchResponseBuilder floorArea(Long floorArea) { this.floorArea = floorArea; return this; }
    public BuildingSearchResponseBuilder rentArea(String rentArea) { this.rentArea = rentArea; return this; }
    public BuildingSearchResponseBuilder rentPrice(Long rentPrice) { this.rentPrice = rentPrice; return this; }
    public BuildingSearchResponseBuilder serviceFee(String serviceFee) { this.serviceFee = serviceFee; return this; }
    public BuildingSearchResponseBuilder brokerageFee(Double brokerageFee) { this.brokerageFee = brokerageFee; return this; }
    public BuildingSearchResponseBuilder numberOfBasement(String numberOfBasement) { this.numberOfBasement = numberOfBasement; return this; }

    public BuildingSearchResponse build() {
        BuildingSearchResponse response = new BuildingSearchResponse();
        response.setId(this.id);
        response.setName(this.name);
        response.setAddress(this.address);
        response.setManagerName(this.managerName);
        response.setManagerPhoneNumber(this.managerPhoneNumber);
        response.setFloorArea(this.floorArea);
        response.setRentArea(this.rentArea);
        response.setRentPrice(this.rentPrice);
        response.setServiceFee(this.serviceFee);
        response.setBrokerageFee(this.brokerageFee);
        response.setNumberOfBasement(this.numberOfBasement);
        return response;
    }
}
