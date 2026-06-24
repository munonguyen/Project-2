package com.devon.building.model.dto;


import jakarta.validation.constraints.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@FieldDefaults(level= AccessLevel.PRIVATE)

public class BuildingDTO {
    static final long serialVersionUID = 1L;

    public interface Create {
    }

    Long id;

    @NotBlank(message="Tên tòa nhà không được để trống", groups = Create.class)
    String name;

    String street;
    String ward;
    Long numberOfBasement;

    @NotNull(message="Quận không được để trống", groups = Create.class)
    String districtId;

    @NotNull(message="Giá thuê không được để trống", groups = Create.class)
    @Min(value=0,message="Giá thuê phải lớn hơn hoặc bằng 0", groups = Create.class)
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

    @Pattern(regexp="^\\s*$|\\d{10}",message="Số điện thoại quản lý phải có 10 chữ số", groups = Create.class)
    String managerPhoneNumber;

    Long level;

    @NotBlank(message="Diện tích thuê không được để trống", groups = Create.class)
    String rentArea;

    @NotEmpty(message="Mã loại tòa nhà là bắt buộc", groups = Create.class)
    List<String> typeCode;

}
