package com.devon.building;

import com.devon.building.model.dto.ResponseDTO;
import com.devon.building.service.BuildingService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AuthorizationIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
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
}
