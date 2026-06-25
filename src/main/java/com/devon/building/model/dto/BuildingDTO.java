package com.devon.building.model.dto;

import jakarta.validation.constraints.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)

public class BuildingDTO {
    static final long serialVersionUID = 1L;


    Long id;

    @NotBlank(message = "Tên tòa nhà không được để trống")
    String name;

    String street;
    String ward;
    Long numberOfBasement;

    @NotBlank(message = "Quận không được để trống")
    @Pattern(regexp = "QUAN_1|QUAN_2|QUAN_3|QUAN_4|QUAN_5|QUAN_10|QUAN_11|QUAN_12",
            message = "Quận không hợp lệ")
    String districtId;

    @NotNull(message = "Giá thuê không được để trống")
    @Min(value = 0, message = "Giá thuê phải lớn hơn hoặc bằng 0")
    Long rentPrice;

    Long floorArea;
    String structure;
    String direction;
    String rentPriceDescription;
    String serviceFee;
    String carFee;
    String overTimeFee;
    Double brokerageFee;
    String note;
    byte[] image;
    String uploadImage;
    String managerName;

    @Pattern(regexp = "^\\s*$|\\d{10}", message = "Số điện thoại quản lý phải có 10 chữ số")
    String managerPhoneNumber;

    Long level;

    @NotBlank(message = "Diện tích thuê không được để trống")
    @Pattern(regexp = "^\\s*\\d+(\\s*,\\s*\\d+)*\\s*$", message = "Diện tích thuê không hợp lệ (VD: 100,200,300)")
    String rentArea;

    @NotEmpty(message = "Mã loại tòa nhà là bắt buộc")
    List<String> typeCode;

}
