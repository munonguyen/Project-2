package com.devon.building.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TransactionDTO {
    Long id;

    @NotNull(message = "Khách hàng không được để trống")
    Long customerId;

    @NotBlank(message = "Mã giao dịch không được để trống")
    String code;

    @NotBlank(message = "Phải nhập chi tiết giao dịch hợp lệ")
    String note;
}
