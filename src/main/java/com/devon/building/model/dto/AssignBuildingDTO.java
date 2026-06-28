package com.devon.building.model.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class AssignBuildingDTO {
    @NotNull(message="Không tìm thấy toà nhà")
    private Long buildingId;
    private List<Long> staffIds = new ArrayList<>();

}
