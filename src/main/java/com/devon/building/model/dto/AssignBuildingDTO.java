package com.devon.building.model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
public class AssignBuildingDTO {
    @NotNull(message = "Building id not found")
    Long buildingId;
    List<Long> staffIds = new ArrayList<>();
}
