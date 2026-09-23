package com.devon.building.builder;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BuildingSearchBuilder {
    final String name;
    final Long floorArea;
    final String district;
    final String ward;
    final String street;
    final Integer numberOfBasement;
    final String direction;
    final String level;
    final Long areaFrom;
    final Long areaTo;
    final Long rentPriceFrom;
    final Long rentPriceTo;
    final String managerName;
    final String managerPhone;
    final List<String> typeCode;
    final Long staffId;
}
