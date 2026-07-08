package com.devon.building.model.dto;

import jakarta.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AssignBuildingDTO {
  @NotNull(message = "Không tìm thấy toà nhà")
  private Long buildingId;

  private List<Long> staffIds = new ArrayList<>();
}
