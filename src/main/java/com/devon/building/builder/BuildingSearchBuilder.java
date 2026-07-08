package com.devon.building.builder;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BuildingSearchBuilder {
  private String name;
  private Long floorArea;
  private String district;
  private String ward;
  private String street;
  private String numberOfBasement;
  private String direction;
  private String level;
  private Long areaFrom;
  private Long areaTo;
  private Long rentPriceFrom;
  private Long rentPriceTo;
  private String managerName;
  private String managerPhone;
  private Long staffId;
  private List<String> typeCode;
}
