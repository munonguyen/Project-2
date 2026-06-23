package com.devon.building.model.dto.Request;

import com.devon.building.entity.Building;
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
    String numberOfBasement;
    String direction;
    String level;
    Long rentAreaFrom;
    Long rentAreaTo;
    Long rentPriceFrom;
    Long rentPriceTo;
    String managerName;
    String managePhoneNumber;
    Long staffId;
    List<String> typeCodes;

    public boolean matches(Building b) {
        return (name == null || name.isBlank() || b.getName().toLowerCase().contains(name.toLowerCase()))
                && (district == null || district.isBlank() || district.equalsIgnoreCase(b.getDistrict()))
                && (ward == null || ward.isBlank() || (b.getWard() != null && b.getWard().toLowerCase().contains(ward.toLowerCase())))
                && (street == null || street.isBlank() || (b.getStreet() != null && b.getStreet().toLowerCase().contains(street.toLowerCase())))
                && (floorArea == null || floorArea.equals(b.getFloorArea()))
                && (numberOfBasement == null || numberOfBasement.isBlank() || (b.getNumberOfBasement() != null && numberOfBasement.equals(String.valueOf(b.getNumberOfBasement()))))
                && (direction == null || direction.isBlank() || direction.equalsIgnoreCase(b.getDirection()))
                && (level == null || level.isBlank() || level.equalsIgnoreCase(b.getLevel()))
                && (rentPriceFrom == null || (long) b.getPrice() >= rentPriceFrom)
                && (rentPriceTo == null || (long) b.getPrice() <= rentPriceTo)
                && (managerName == null || managerName.isBlank() || (b.getManagerName() != null && b.getManagerName().toLowerCase().contains(managerName.toLowerCase())))
                && (managePhoneNumber == null || managePhoneNumber.isBlank() || (b.getManagerPhoneNumber() != null && b.getManagerPhoneNumber().contains(managePhoneNumber)))
                && (typeCodes == null || typeCodes.isEmpty() || (b.getType() != null && typeCodes.stream().anyMatch(b.getType()::contains)))
                && (staffId == null || b.getUser().stream().anyMatch(u -> staffId.equals(u.getId())))
                && ((rentAreaFrom == null && rentAreaTo == null) || b.getRentAreas().stream().anyMatch(ra ->
                        (rentAreaFrom == null || ra.getValue() >= rentAreaFrom)
                        && (rentAreaTo == null || ra.getValue() <= rentAreaTo)));
    }
}
