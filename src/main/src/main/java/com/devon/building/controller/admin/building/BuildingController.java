package com.devon.building.controller.admin.building;

import com.devon.building.entity.Building;
import com.devon.building.enums.District;
import com.devon.building.enums.RentType;
import com.devon.building.model.dto.BuildingDTO;
import com.devon.building.model.dto.Request.BuildingSearchRequest;
import com.devon.building.model.dto.response.BuildingSearchResponse;
import com.devon.building.repository.BuildingRepository;
import com.devon.building.service.BuildingService;
import com.devon.building.service.UserService;
import com.devon.building.converter.BuildingConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import java.util.List;
import static com.devon.building.constant.SystemConstant.DISTRICT;
import static com.devon.building.constant.SystemConstant.RENT_TYPE;

@Controller
@RequestMapping("/admin/buildings")
@RequiredArgsConstructor
public class BuildingController {

    private final UserService userService;
    private final BuildingRepository buildingRepository;
    private final BuildingConverter buildingConverter;
    private final BuildingService buildingService;

    @GetMapping("/list")
    public ModelAndView getAllBuilding(@ModelAttribute BuildingSearchRequest buildingSearchRequest) {
        ModelAndView modelAndView = new ModelAndView("admin/building/buildingList");
        modelAndView.addObject("staffs", userService.loadStaffs());
        modelAndView.addObject(DISTRICT, District.getDistrictMap());
        modelAndView.addObject(RENT_TYPE, RentType.getRentTypeMap());
        List<BuildingSearchResponse> result = buildingService.getAllBuildings(buildingSearchRequest);
        modelAndView.addObject("buildingList", result);
        return modelAndView;
    }

    @GetMapping("/edit")
    public ModelAndView getEditbuilding() {
        ModelAndView modelAndView = new ModelAndView("admin/building/buildingEdit");
        modelAndView.addObject(DISTRICT, District.getDistrictMap());
        modelAndView.addObject(RENT_TYPE, RentType.getRentTypeMap());
        modelAndView.addObject("building", new BuildingDTO());

        return modelAndView;
    }

    @GetMapping("/{id}/update")
    public ModelAndView getUpdateBuildings(@PathVariable Long id) {
        ModelAndView modelAndView = new ModelAndView("admin/building/buildingEdit");
        modelAndView.addObject(DISTRICT, District.getDistrictMap());
        modelAndView.addObject(RENT_TYPE, RentType.getRentTypeMap());
        Building building = buildingRepository.findById(id).orElse(null);
        if (building == null) {
            modelAndView.setViewName("redirect:/admin/buildings/list");
            return modelAndView;
        }
        modelAndView.addObject("building", buildingConverter.toBuildingDTO(building));
        return modelAndView;
    }
}
