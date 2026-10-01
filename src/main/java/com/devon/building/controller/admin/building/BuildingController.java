package com.devon.building.controller.admin.building;

import com.devon.building.constant.SystemConstant;
import com.devon.building.entity.User;
import com.devon.building.enums.District;
import com.devon.building.enums.RentType;
import com.devon.building.model.dto.BuildingDTO;
import com.devon.building.model.request.BuildingSearchRequest;
import com.devon.building.model.response.BuildingSearchResponse;
import com.devon.building.pagination.PaginationResult;
import com.devon.building.service.BuildingService;
import com.devon.building.service.UserService;
import com.devon.building.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/admin/buildings")
@RequiredArgsConstructor
public class BuildingController {

    private static final String DISTRICT = "districts";
    private static final String RENT_TYPE = "rentTypes";

    private final UserService userService;
    private final BuildingService buildingService;

    @GetMapping("/list")
    public ModelAndView getAllBuildings(
            @RequestParam(value = "page", defaultValue = "1") String pageStr,
            @ModelAttribute BuildingSearchRequest buildingSearchRequest) {
        ModelAndView modelAndView = new ModelAndView("admin/building/buildingList");

        if (SecurityUtils.getAuthorities().contains(SystemConstant.STAFF_ROLE)) {
            User staff = userService.getUserInfo(SecurityUtils.getCurrentUsername());
            if (staff != null) {
                buildingSearchRequest.setStaffId(staff.getId());
            }
        }

        int page;
        try {
            page = Integer.parseInt(pageStr);
        } catch (NumberFormatException e) {
            page = 1;
        }
        buildingSearchRequest.setPage(page);

        modelAndView.addObject("staffs", userService.loadStaff());
        modelAndView.addObject(DISTRICT, District.getDistrictMap());
        modelAndView.addObject(RENT_TYPE, RentType.getRentTypeMap());
        modelAndView.addObject("buildingSearchRequest", buildingSearchRequest);

        PaginationResult<BuildingSearchResponse> result = buildingService.findBuilding(
                buildingSearchRequest,
                buildingSearchRequest.getPage(),
                SystemConstant.MAX_PAGE_ITEM,
                SystemConstant.MAX_NAVIGATION_PAGE);
        modelAndView.addObject("result", result);
        return modelAndView;
    }

    @GetMapping("/edit")
    public ModelAndView getEditBuildings() {
        ModelAndView modelAndView = new ModelAndView("admin/building/buildingEdit");
        modelAndView.addObject(DISTRICT, District.getDistrictMap());
        modelAndView.addObject(RENT_TYPE, RentType.getRentTypeMap());
        modelAndView.addObject("building", new BuildingDTO());
        return modelAndView;
    }

    @GetMapping("/{id}/update")
    public ModelAndView getUpdateBuilding(@PathVariable Long id) {
        ModelAndView modelAndView = new ModelAndView("admin/building/buildingEdit");
        modelAndView.addObject("building", buildingService.findById(id));
        modelAndView.addObject(DISTRICT, District.getDistrictMap());
        modelAndView.addObject(RENT_TYPE, RentType.getRentTypeMap());
        return modelAndView;
    }
}
