package com.devon.building.enums;

import lombok.Getter;

import java.util.LinkedHashMap;
import java.util.Map;

@Getter
public enum Transaction {
    CSKH("Chăm sóc khách hàng"),
    DDX("Dẫn đi xem");

    private final String name;
    Transaction(String name){
        this.name=name;
    }
    public static Map<String, String> getStatus() {
        Map<String, String> transactionTypes = new LinkedHashMap<>();
        for (Transaction t : Transaction.values()) {
            transactionTypes.put(t.name(), t.getName());
        }
        return transactionTypes;
    }

}
