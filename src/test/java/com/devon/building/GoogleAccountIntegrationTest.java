package com.devon.building;

import com.devon.building.constant.SystemConstant;
import com.devon.building.entity.User;
import com.devon.building.repository.UserRepository;
import com.devon.building.service.impl.CustomOid2UserService;
import com.devon.building.utils.OAuth2PictureFetcher;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
class GoogleAccountIntegrationTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CustomOid2UserService customOid2UserService;

    @MockBean
    private OAuth2PictureFetcher pictureFetcher;

    private final List<Long> createdUserIds = new ArrayList<>();

    @BeforeEach
    void setUp() {
        when(pictureFetcher.fetchGoogleProfilePicture(anyString())).thenReturn(null);
    }

    @AfterEach
    void tearDown() {
        for (Long id : createdUserIds) {
            try {
                userRepository.deleteById(id);
            } catch (Exception ignored) {
            }
        }
    }

    @Test
    void testSaveAndFindByGoogleAccountId() {
        String testGoogleId = "109876543210987654321"; // 21-digit Google sub ID
        String testUsername = "google_user_test_" + System.currentTimeMillis();

        User user = new User();
        user.setUserName(testUsername);
        user.setEmail(testUsername + "@gmail.com");
        user.setFullName("Google Test User");
        user.setGoogleAccountId(testGoogleId);
        user.setUserRole(SystemConstant.USER_ROLE);
        user.setActive(true);
        user.setEncrytedPassword("encrypted_dummy_password");

        User savedUser = userRepository.save(user);
        createdUserIds.add(savedUser.getId());

        assertNotNull(savedUser.getId());
        assertEquals(testGoogleId, savedUser.getGoogleAccountId());

        User foundByGoogleId = userRepository.findByGoogleAccountId(testGoogleId);
        assertNotNull(foundByGoogleId);
        assertEquals(savedUser.getId(), foundByGoogleId.getId());
        assertEquals(testUsername, foundByGoogleId.getUserName());

        User foundByEmail = userRepository.findByEmail(testUsername + "@gmail.com");
        assertNotNull(foundByEmail);
        assertEquals(savedUser.getId(), foundByEmail.getId());
    }

    @Test
    void testMultipleUsersWithNullGoogleAccountIdDoNotViolateUniqueConstraint() {
        String u1 = "normal_user_1_" + System.currentTimeMillis();
        String u2 = "normal_user_2_" + System.currentTimeMillis();

        User user1 = new User();
        user1.setUserName(u1);
        user1.setFullName("Normal User 1");
        user1.setGoogleAccountId(null);
        user1.setUserRole(SystemConstant.USER_ROLE);
        user1.setActive(true);
        user1.setEncrytedPassword("pwd1");

        User user2 = new User();
        user2.setUserName(u2);
        user2.setFullName("Normal User 2");
        user2.setGoogleAccountId(null);
        user2.setUserRole(SystemConstant.USER_ROLE);
        user2.setActive(true);
        user2.setEncrytedPassword("pwd2");

        User saved1 = userRepository.save(user1);
        User saved2 = userRepository.save(user2);
        createdUserIds.add(saved1.getId());
        createdUserIds.add(saved2.getId());

        assertNotNull(saved1.getId());
        assertNotNull(saved2.getId());
        assertNull(saved1.getGoogleAccountId());
        assertNull(saved2.getGoogleAccountId());
    }

    @Test
    void testAccountLinkingWhenExistingUserLogsInWithGoogle() {
        String existingEmail = "link_test_" + System.currentTimeMillis() + "@gmail.com";
        String googleSubId = "112233445566778899001";

        // User previously registered without Google
        User localUser = new User();
        localUser.setUserName(existingEmail);
        localUser.setEmail(existingEmail);
        localUser.setFullName("Local Registered User");
        localUser.setGoogleAccountId(null);
        localUser.setUserRole(SystemConstant.USER_ROLE);
        localUser.setActive(true);
        localUser.setEncrytedPassword("local_pwd_hash");
        localUser = userRepository.save(localUser);
        createdUserIds.add(localUser.getId());

        assertNull(localUser.getGoogleAccountId());

        // Now find by email and simulate account linking
        User userByEmail = userRepository.findByEmail(existingEmail);
        assertNotNull(userByEmail);
        userByEmail.setGoogleAccountId(googleSubId);
        userRepository.save(userByEmail);

        // Verify linked Google account can be found
        User linkedUser = userRepository.findByGoogleAccountId(googleSubId);
        assertNotNull(linkedUser);
        assertEquals(localUser.getId(), linkedUser.getId());
        assertEquals(googleSubId, linkedUser.getGoogleAccountId());
    }
}
