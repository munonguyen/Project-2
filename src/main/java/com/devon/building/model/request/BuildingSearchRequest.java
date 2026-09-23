package com.devon.building.model.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BuildingSearchRequest {
    String name;
    Long floorArea;
    String district;
    String ward;
    String street;
    Integer numberOfBasement;
    String direction;
    String level;
    Long areaFrom;
    Long areaTo;
    Long rentPriceFrom;
    Long rentPriceTo;
    String managerName;
    String managerPhone;
    Long staffId;
    List<String> typeCode;
    Integer page = 1;
}
