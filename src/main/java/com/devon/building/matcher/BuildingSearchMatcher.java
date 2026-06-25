package com.devon.building.matcher;

import com.devon.building.entity.Building;
import com.devon.building.model.dto.Request.BuildingSearchRequest;
import org.springframework.stereotype.Component;

@Component
public class BuildingSearchMatcher {

    public boolean matches(Building building, BuildingSearchRequest request) {
        if (!containsIgnoreCase(building.getName(), request.getName())) {
            return false;
        }
        if (!equalsIgnoreCase(building.getDistrict(), request.getDistrict())) {
            return false;
        }
        if (!containsIgnoreCase(building.getWard(), request.getWard())) {
            return false;
        }
        if (!containsIgnoreCase(building.getStreet(), request.getStreet())) {
            return false;
        }
        if (request.getFloorArea() != null && !request.getFloorArea().equals(building.getFloorArea())) {
            return false;
        }
        if (!matchesNumberOfBasement(building, request)) {
            return false;
        }
        if (!equalsIgnoreCase(building.getDirection(), request.getDirection())) {
            return false;
        }
        if (!equalsIgnoreCase(building.getLevel(), request.getLevel())) {
            return false;
        }
        if (request.getRentPriceFrom() != null && (long) building.getPrice() < request.getRentPriceFrom()) {
            return false;
        }
        if (request.getRentPriceTo() != null && (long) building.getPrice() > request.getRentPriceTo()) {
            return false;
        }
        if (!containsIgnoreCase(building.getManagerName(), request.getManagerName())) {
            return false;
        }
        if (!contains(building.getManagerPhoneNumber(), request.getManagePhoneNumber())) {
            return false;
        }
        if (!matchesTypeCode(building, request)) {
            return false;
        }
        if (!matchesStaff(building, request)) {
            return false;
        }
        return matchesRentArea(building, request);
    }

    private boolean containsIgnoreCase(String buildingValue, String searchValue) {
        if (searchValue == null || searchValue.isBlank()) {
            return true;
        }
        return buildingValue != null
                && buildingValue.toLowerCase().contains(searchValue.toLowerCase());
    }

    private boolean equalsIgnoreCase(String buildingValue, String searchValue) {
        if (searchValue == null || searchValue.isBlank()) {
            return true;
        }
        return buildingValue != null && searchValue.equalsIgnoreCase(buildingValue);
    }

    private boolean contains(String buildingValue, String searchValue) {
        if (searchValue == null || searchValue.isBlank()) {
            return true;
        }
        return buildingValue != null && buildingValue.contains(searchValue);
    }

    private boolean matchesNumberOfBasement(Building building, BuildingSearchRequest request) {
        String numberOfBasement = request.getNumberOfBasement();
        if (numberOfBasement == null || numberOfBasement.isBlank()) {
            return true;
        }
        return building.getNumberOfBasement() != null
                && numberOfBasement.equals(String.valueOf(building.getNumberOfBasement()));
    }

    private boolean matchesTypeCode(Building building, BuildingSearchRequest request) {
        if (request.getTypeCodes() == null || request.getTypeCodes().isEmpty()) {
            return true;
        }
        return building.getType() != null
                && request.getTypeCodes().stream().anyMatch(building.getType()::contains);
    }

    private boolean matchesStaff(Building building, BuildingSearchRequest request) {
        if (request.getStaffId() == null) {
            return true;
        }
        return building.getAssignmentBuildings().stream()
                .anyMatch(assignment -> request.getStaffId().equals(assignment.getStaff().getId()));
    }

    private boolean matchesRentArea(Building building, BuildingSearchRequest request) {
        if (request.getRentAreaFrom() == null && request.getRentAreaTo() == null) {
            return true;
        }
        return building.getRentAreas().stream().anyMatch(rentArea ->
                (request.getRentAreaFrom() == null || rentArea.getValue() >= request.getRentAreaFrom())
                        && (request.getRentAreaTo() == null || rentArea.getValue() <= request.getRentAreaTo()));
    }
}
