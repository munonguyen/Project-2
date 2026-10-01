package com.devon.building;

import com.devon.building.model.dto.ResponseDTO;
import com.devon.building.service.BuildingService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AuthorizationIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BuildingService buildingService;

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void regularUserCannotAccessStaffBuildingApi() throws Exception {
        mockMvc.perform(get("/api/buildings/1/staff"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "staff", roles = "STAFF")
    void staffCanAccessStaffBuildingApi() throws Exception {
        when(buildingService.loadStaffs(1L)).thenReturn(new ResponseDTO());

        mockMvc.perform(get("/api/buildings/1/staff"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "manager", roles = "MANAGER")
    void managerCanAccessStaffBuildingApi() throws Exception {
        when(buildingService.loadStaffs(1L)).thenReturn(new ResponseDTO());

        mockMvc.perform(get("/api/buildings/1/staff"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "manager", roles = "MANAGER")
    void managerMutationRequiresCsrfForSessionAuthentication() throws Exception {
        mockMvc.perform(delete("/api/buildings/1"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "manager", roles = "MANAGER")
    void managerCanDeleteBuildingWithCsrf() throws Exception {
        when(buildingService.deleteBuilding(List.of(1L))).thenReturn(new ResponseDTO());

        mockMvc.perform(delete("/api/buildings/1").with(csrf()))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "staff", roles = "STAFF")
    void staffCannotDeleteBuildingEvenWithCsrf() throws Exception {
        mockMvc.perform(delete("/api/buildings/1").with(csrf()))
                .andExpect(status().isForbidden());
    }
}
