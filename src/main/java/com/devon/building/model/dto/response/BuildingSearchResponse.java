package com.devon.building.model.dto.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BuildingSearchResponse {
  Long id;
  String name;
  String address; // street,ward,district
  String numberOfBasement;
  String managerName;
  String managerPhoneNumber;
  Long floorArea;
  String rentArea; // "100,200,300"
  Long emptyArea;
  Long rentPrice;
  String serviceFee;
  Double brokerageFee;
}
