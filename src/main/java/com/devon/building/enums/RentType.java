package com.devon.building.enums;

import lombok.Getter;

import java.util.LinkedHashMap;
import java.util.Map;

@Getter
public enum RentType {
    TANG_TRET("Tầng Trệt"),
    NGUYEN_CAN("Nguyên Căn"),
    NOI_THAT("Nội Thất");


    private final String name;

    RentType(String name){
        this.name = name;
    }
    public static Map<String,String> getRentTypeMap(){
        Map<String,String> rentType = new LinkedHashMap();
        for(RentType type:RentType.values()){
            rentType.put(type.toString(),type.name);
        }
        return rentType;
    }
}
