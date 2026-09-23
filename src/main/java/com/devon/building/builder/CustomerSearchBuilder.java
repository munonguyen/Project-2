package com.devon.building.builder;

import com.devon.building.enums.Status;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CustomerSearchBuilder {
    private String fullName;
    private String phoneNumber;
    private String email;
    private Long staffId;
    private Status status;
}
