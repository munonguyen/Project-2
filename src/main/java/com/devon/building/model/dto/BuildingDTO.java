package com.devon.building.model.dto;

import jakarta.validation.constraints.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
public class BuildingDTO {

    Long id;
    @NotBlank(message = "Tên tòa nhà không được để trống")
    String name;
    String street;
    String ward;
    @NotBlank(message = "Quận không được để trống")
    String district;
    String structure;
    @Min(value = 0, message = "Số tầng hầm phải lớn hơn hoặc bằng 0")
    Integer numberOfBasement;
    Long floorArea;
    String direction;
    String level;
    @NotNull(message = "Gía thuê không được để trống")
    @Min(value = 0, message = "Giá thuê phải lớn hơn hoặc bằng 0")
    Long price;
    String rentPriceDescription;
    String serviceFee;
    String carFee;
    String motoFee;
    String overTimeFee;
    Double brokerageFee;
    String managerName;
    @Pattern(regexp = "^$|^\\d{10}$", message = "Số điện thoại không đúng định dạng")
    String managerPhone;
    @NotBlank(message = "Diện tích thuê không được để trống")
    @Pattern(
            regexp = "^\\d++(?:, ?\\d++)*+$",
            message = "Nhập lại có dạng: 100, 200, 300 hoặc 100,200,300"
    )
    String rentArea;
    @NotEmpty(message = "Loại tòa nhà không được để trống")
    List<String> typeCode;
}
