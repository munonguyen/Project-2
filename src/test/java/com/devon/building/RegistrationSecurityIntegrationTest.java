package com.devon.building;

import com.devon.building.constant.SystemConstant;
import com.devon.building.entity.User;
import com.devon.building.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class RegistrationSecurityIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    private final List<Long> createdUserIds = new ArrayList<>();

    @AfterEach
    void tearDown() {
        userRepository.deleteAllById(createdUserIds);
    }

    @Test
    void publicRegistrationCannotEscalateRoleThroughRoleIdOrRoleCode() throws Exception {
        String username = "security_" + UUID.randomUUID().toString().replace("-", "").substring(0, 12);

        String payload = """
                {
                  "fullname": "Security Test User",
                  "username": "%s",
                  "phone_number": "0900000000",
                  "address": "Test address",
                  "password": "SecurePass123!",
                  "retype_password": "SecurePass123!",
                  "roleId": 1,
                  "role_id": 1,
                  "roleCode": "ROLE_MANAGER",
                  "userRole": "ROLE_MANAGER"
                }
                """.formatted(username);

        mockMvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.role").value(SystemConstant.USER_ROLE));

        User created = userRepository.findByUserName(username);
        assertNotNull(created);
        createdUserIds.add(created.getId());
        assertEquals(SystemConstant.USER_ROLE, created.getUserRole());
    }
}
