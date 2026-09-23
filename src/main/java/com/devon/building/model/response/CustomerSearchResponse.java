package com.devon.building.model.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import java.util.Date;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CustomerSearchResponse {
    Long id;
    String fullName;
    String phoneNumber;
    String email;
    String demand;
    String createdBy;
    Date createdDate;
    String status;
}
