package com.devon.building.enums;

import lombok.Getter;

import java.util.LinkedHashMap;
import java.util.Map;

@Getter
public enum RentType {
    TANG_TRET("Tầng trệt"),
    NOI_THAT("Nội thất"),
    NGUYEN_CAN("Nguyên căn");

    private final String name;

    RentType(String name){
        this.name = name;
    }

    public static Map<String, String> getRentTypeMap(){
        Map<String, String> type = new LinkedHashMap<>();
        for(RentType rentType : RentType.values()){
            type.put(rentType.toString(), rentType.getName());
        }
        return type;
    }

}
