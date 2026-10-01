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

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserDetailsServiceImpl implements UserDetailsService {

    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUserNameAndActiveTrue(username);

        if (user == null) {
            log.warn("Active user '{}' was not found", username);
            throw new UsernameNotFoundException("User " + username + " was not found or is inactive");
        }

        List<GrantedAuthority> grantList = new ArrayList<>();
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
                Boolean.TRUE.equals(user.getActive()),
                true,
                true,
                true,
                grantList);
    }
}
