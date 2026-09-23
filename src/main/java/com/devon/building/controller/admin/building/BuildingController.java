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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/admin/buildings")
@RequiredArgsConstructor
public class BuildingController {

    private final UserService userService;
    private final BuildingService buildingService;
    private static final String DISTRICT = "districts";
    private static final String RENT_TYPE = "rentTypes";

    @GetMapping("/list")
    public ModelAndView getAllBuildings(@RequestParam(value = "page", defaultValue = "1") String pageStr, @ModelAttribute BuildingSearchRequest buildingSearchRequest){
        ModelAndView modelAndView = new ModelAndView("admin/building/buildingList");

        if(SecurityUtils.getAuthorities().contains(SystemConstant.STAFF_ROLE)){
            User user = userService.getUserInfo(SecurityUtils.getCurrentUsername());
            buildingSearchRequest.setStaffId(user.getId());
        }
        int page = 1;
        try{
            page = Integer.parseInt(pageStr);
        }catch(Exception e){
            e.printStackTrace();
        }
        buildingSearchRequest.setPage(page);
        modelAndView.addObject("staffs", userService.loadStaff());
        modelAndView.addObject(DISTRICT, District.getDistrictMap());
        modelAndView.addObject(RENT_TYPE, RentType.getRentTypeMap());
        modelAndView.addObject("buildingSearchRequest", buildingSearchRequest);
        if(SecurityUtils.getAuthorities().contains(SystemConstant.STAFF_ROLE)){
            User staff = userService.getUserInfo(SecurityUtils.getCurrentUsername());
            buildingSearchRequest.setStaffId(staff.getId());
        }
        PaginationResult<BuildingSearchResponse> result = buildingService.findBuilding(buildingSearchRequest, buildingSearchRequest.getPage(), SystemConstant.MAX_PAGE_ITEM, SystemConstant.MAX_NAVIGATION_PAGE);
        modelAndView.addObject("result", result);

        return modelAndView;
    }

    @GetMapping("/edit")
    public ModelAndView getEditBuildings(@ModelAttribute("buildingEdit") BuildingDTO buildingDTO){
        ModelAndView modelAndView = new ModelAndView("admin/building/buildingEdit");
        modelAndView.addObject(DISTRICT, District.getDistrictMap());
        modelAndView.addObject(RENT_TYPE, RentType.getRentTypeMap());
        modelAndView.addObject("building", new BuildingDTO());
        return modelAndView;
    }

    @GetMapping("/{id}/update")
    public ModelAndView getUpdateBuilding(@PathVariable Long id){
        ModelAndView modelAndView = new ModelAndView("admin/building/buildingEdit");
        BuildingDTO buildingDTO = buildingService.findById(id);
        modelAndView.addObject("building", buildingDTO);
        modelAndView.addObject(DISTRICT, District.getDistrictMap());
        modelAndView.addObject(RENT_TYPE, RentType.getRentTypeMap());
        return modelAndView;
    }
}
