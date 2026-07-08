package com.devon.building.model.dto.Request;

import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BuildingSearchRequest {
  String name;
  Long floorArea;
  String district;
  String ward;
  String street;
  String numberOfBasement;
  String direction;
  String level;
  Long rentAreaFrom;
  Long rentAreaTo;
  Long rentPriceFrom;
  Long rentPriceTo;
  String managerName;
  String managerPhoneNumber;
  Long staffId;
  List<String> typeCodes;
}
