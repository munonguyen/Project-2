package com.devon.building.enums;

import java.util.LinkedHashMap;
import java.util.Map;

public enum District {
    QUAN_1("Quan 1"),
    QUAN_2("Quan 2"),
    QUAN_3("Quan 3"),
    QUAN_4("Quan 4"),
    QUAN_5("Quan 5"),
    QUAN_10("Quan 10"),
    QUAN_11("Quan 11"),
    QUAN_12("Quan 12"),
    ;

    private final String districtName;

    District(String districtName) {
        this.districtName = districtName;
    }

    public static Map<String, String> getDistrictMap() {
        Map<String, String> districtMap = new LinkedHashMap<>();
        for (District district : District.values()) {
            districtMap.put(district.toString(), district.districtName);
        }

        return districtMap;
    }
}
