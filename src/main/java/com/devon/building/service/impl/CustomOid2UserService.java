package com.devon.building.service.impl;

import com.devon.building.constant.SystemConstant;
import com.devon.building.entity.User;
import com.devon.building.repository.UserRepository;
import com.devon.building.security.CustomOAuth2User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomOid2UserService extends OidcUserService {

    private final UserRepository userRepository;
    private final com.devon.building.utils.OAuth2PictureFetcher pictureFetcher;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);

    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
        OidcUser oidcUser = super.loadUser(userRequest);
        log.info("OidcUser attributes: {}", oidcUser.getAttributes());
        String email = extractEmail(oidcUser);

        User user;
        try {
            user = userRepository.findByUserName(email);
            if (user == null) {
                user = createGoogleUser(oidcUser);
            }

            if (Boolean.FALSE.equals(user.getActive())) {
                log.warn("Login attempt for inactive user: {}", email);
                throw new OAuth2AuthenticationException("Account must be active");
            }
        } catch (OAuth2AuthenticationException ex) {
            throw ex;
        } catch (Exception ex) {
            log.error("Failed to load or create user for email: {}", email, ex);
            throw new OAuth2AuthenticationException(new OAuth2Error("server_error"), "Error persisting user: " + ex.getMessage());
        }

        return new CustomOAuth2User(user, oidcUser, buildAuthorities(user));
    }

    private User createGoogleUser(OidcUser oidcUser) {
        User user = new User();
        user.setUserName(oidcUser.getEmail());
        user.setFullName(oidcUser.getFullName());
        user.setGoogleAccountId(oidcUser.getAttributes().get("sub").toString());
        user.setPhone("");
        user.setUserRole(SystemConstant.USER_ROLE);
        user.setActive(oidcUser.getEmailVerified());
        user.setEncrytedPassword(passwordEncoder.encode(UUID.randomUUID().toString()));

        String pictureUrl = oidcUser.getPicture() != null ? oidcUser.getPicture() : oidcUser.getAttribute("picture");
        if (pictureUrl != null && !pictureUrl.isBlank()) {
            byte[] image = pictureFetcher.fetchGoogleProfilePicture(pictureUrl);
            if (image != null) {
                user.setImage(image);
            }
        }

        log.info("Registered new user from Google OIDC: {}", user.getEmail());
        return userRepository.save(user);
    }

    private String extractEmail(OidcUser oidcUser) {
        String email = oidcUser.getEmail();
        if (email != null && !email.isBlank()) {
            return email.trim();
        }
        String attrEmail = oidcUser.getAttribute("email");
        if (attrEmail != null && !attrEmail.isBlank()) {
            return attrEmail.trim();
        }
        String name = oidcUser.getFullName();
        if (name != null && !name.isBlank()) {
            return name.trim();
        }
        return Optional.ofNullable(oidcUser.getSubject()).orElse("google_user");
    }

    private List<GrantedAuthority> buildAuthorities(User user) {
        String role = user.getUserRole();
        if (role != null && !role.isBlank()) {
            String authority = role.startsWith("ROLE_") ? role : "ROLE_" + role;
            return List.of(new SimpleGrantedAuthority(authority));
        }
        return List.of(new SimpleGrantedAuthority(SystemConstant.USER_ROLE));
    }
}
