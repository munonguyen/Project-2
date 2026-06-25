package com.devon.building.validator;

import com.devon.building.enums.District;
import com.devon.building.enums.RentType;
import com.devon.building.model.dto.BuildingDTO;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Component
public class BuildingValidator {

    public List<String> validate(BuildingDTO buildingDTO, boolean update) {
        List<String> details = new ArrayList<>();
        validateName(buildingDTO, update, details);
        validateDistrict(buildingDTO, details);
        validateRentPrice(buildingDTO, update, details);
        validateManagerPhoneNumber(buildingDTO, update, details);
        validateTypeCode(buildingDTO, update, details);
        validateRentArea(buildingDTO, details);
        validateUploadImage(buildingDTO, details);
        return details;
    }

    private void validateName(BuildingDTO buildingDTO, boolean update, List<String> details) {
        if(update && buildingDTO.getName() != null && buildingDTO.getName().isBlank()){
            details.add("name: Tên tòa nhà không được để trống");
        }
    }

    private void validateDistrict(BuildingDTO buildingDTO, List<String> details) {
        if(buildingDTO.getDistrictId() == null){
            return;
        }
        if(buildingDTO.getDistrictId().isBlank()){
            details.add("districtId: Quận không được để trống");
            return;
        }
        if(!District.getDistrictMap().containsKey(buildingDTO.getDistrictId())){
            details.add("districtId: Quận không hợp lệ");
        }
    }

    private void validateRentPrice(BuildingDTO buildingDTO, boolean update, List<String> details) {
        if(update && buildingDTO.getRentPrice() != null && buildingDTO.getRentPrice() < 0){
            details.add("rentPrice: Giá thuê phải lớn hơn hoặc bằng 0");
        }
    }

    private void validateManagerPhoneNumber(BuildingDTO buildingDTO, boolean update, List<String> details) {
        String phoneNumber = buildingDTO.getManagerPhoneNumber();
        if(update && phoneNumber != null && !phoneNumber.isBlank() && !phoneNumber.matches("\\d{10}")){
            details.add("managerPhoneNumber: Số điện thoại quản lý phải có 10 chữ số");
        }
    }

    private void validateTypeCode(BuildingDTO buildingDTO, boolean update, List<String> details) {
        if(buildingDTO.getTypeCode() == null){
            return;
        }
        if(buildingDTO.getTypeCode().isEmpty()){
            if(update){
                details.add("typeCode: Mã loại tòa nhà là bắt buộc");
            }
            return;
        }
        List<String> invalidTypes = buildingDTO.getTypeCode().stream()
                .filter(type -> !RentType.getRentTypeMap().containsKey(type))
                .map(String::valueOf)
                .toList();
        if(!invalidTypes.isEmpty()){
            details.add("typeCode: Mã loại tòa nhà không hợp lệ: " + String.join(", ", invalidTypes));
        }
    }

    private void validateRentArea(BuildingDTO buildingDTO, List<String> details) {
        String rentArea = buildingDTO.getRentArea();
        if(rentArea == null || rentArea.isBlank()){
            return;
        }
        for(String value : rentArea.split(",")){
            try {
                Long.parseLong(value.trim());
            } catch (NumberFormatException exception) {
                details.add("rentArea: Diện tích thuê không hợp lệ");
                return;
            }
        }
    }

    private void validateUploadImage(BuildingDTO buildingDTO, List<String> details) {
        String uploadImage = buildingDTO.getUploadImage();
        if(uploadImage == null || uploadImage.isBlank()){
            return;
        }
        String base64Image = uploadImage;
        if(base64Image.contains(",")){
            base64Image = base64Image.split(",", 2)[1];
        }
        try {
            Base64.getDecoder().decode(base64Image);
        } catch (IllegalArgumentException exception) {
            details.add("uploadImage: Ảnh không hợp lệ");
        }
    }
}
