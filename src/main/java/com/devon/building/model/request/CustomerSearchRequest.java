package com.devon.building.model.request;

import com.devon.building.enums.Status;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CustomerSearchRequest {
    String fullName;
    String phoneNumber;
    String email;
    Long staffId;
    Status status;
    Integer pageNum = 1;
    Integer page = 1;

    public Integer getPage() {
        return page != null ? page : (pageNum != null ? pageNum : 1);
    }

    public String getName() {
        return fullName;
    }

    public void setName(String name) {
        this.fullName = name;
    }
}
