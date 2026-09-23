package com.devon.building.enums;

import lombok.Getter;

import java.util.LinkedHashMap;
import java.util.Map;

@Getter
public enum District {
    QUAN_1("Quận 1"),
    QUAN_2("Quận 2"),
    QUAN_3("Quận 3"),
    QUAN_4("Quận 4"),
    QUAN_10("Quận 10"),
    QUAN_11("Quận 11"),
    QUAN_12("Quận 12"),
    QUAN_TB("Quận Tân Bình");

    private final String districtName;

    District(String districtName){
        this.districtName = districtName;
    }

    public static Map<String, String> getDistrictMap(){
        Map<String, String> districtMap = new LinkedHashMap<>();
        for(District district : District.values()){
            districtMap.put(district.toString(), district.districtName);
        }
        return districtMap;
    }

}
