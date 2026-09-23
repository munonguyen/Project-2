package com.devon.building.model.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BuildingSearchResponse {
    Long id;
    String name;
    String address;
    Integer numberOfBasement;
    String managerName;
    String managerPhone;
    Long floorArea;
    Long emptyArea;
    String rentArea;
    Long price;
    String serviceFee;
    Double brokerageFee;
}
