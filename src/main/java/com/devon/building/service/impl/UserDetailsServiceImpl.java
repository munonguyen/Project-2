package com.devon.building.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.devon.building.entity.User;
import com.devon.building.repository.UserRepository;
import com.devon.building.repository.impl.AccountRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserDetailsServiceImpl implements UserDetailsService {

    AccountRepository accountRepository;
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUserNameAndActiveTrue(username);

        if (user == null) {
            log.error("User '{}' was not found in the database", username);
            throw new UsernameNotFoundException("User " + username + " was not found in the database");
        }

        log.info("Loaded user information for: {}", user.getUserName());

        List<GrantedAuthority> grantList = new ArrayList<>();
        // Add role to authorities (e.g. ROLE_MANAGER, ROLE_STAFF)
        String role = user.getUserRole();
        if (role != null && !role.isBlank()) {
            if (!role.startsWith("ROLE_")) {
                role = "ROLE_" + role;
            }
            grantList.add(new SimpleGrantedAuthority(role));
        }

        return new org.springframework.security.core.userdetails.User(
                user.getUserName(),
                user.getEncrytedPassword(),
                user.getActive(),
                true, // accountNonExpired
                true, // credentialsNonExpired
                true, // accountNonLocked
                grantList);
    }
}