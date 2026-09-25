package com.devon.building.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserInfoResponse {
    Long id;

    @JsonProperty("username")
    String userName;

    @JsonProperty("fullname")
    String fullName;

    @JsonProperty("phone_number")
    String phone;

    String address;

    @JsonProperty("date_of_birth")
    LocalDate dateOfBirth;

    String role;

    @JsonProperty("facebook_account_id")
    Long facebookAccountId;

    @JsonProperty("google_account_id")
    String googleAccountId;
}
