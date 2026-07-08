package com.devon.building.service.impl;

import com.devon.building.entity.User;
import com.devon.building.repository.UserRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

  private final UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User user = userRepository.findByUserName(username);

    if (user == null) {
      throw new UsernameNotFoundException(
          "Không tìm thấy người dùng " //
              + username
              + " trong cơ sở dữ liệu");
    }

    // DB đã lưu với prefix ROLE_ (ROLE_MANAGER, ROLE_STAFF, ROLE_USER)
    String role = user.getUserRole();

    List<GrantedAuthority> grantList = new ArrayList<>();

    GrantedAuthority authority = new SimpleGrantedAuthority(role);

    grantList.add(authority);

    boolean enabled = user.isActive();
    boolean accountNonExpired = true;
    boolean credentialsNonExpired = true;
    boolean accountNonLocked = true;

    UserDetails userDetails =
        (UserDetails)
            new org.springframework.security.core.userdetails.User(
                user.getUserName(), //
                user.getPassword(),
                enabled,
                accountNonExpired, //
                credentialsNonExpired,
                accountNonLocked,
                grantList);

    return userDetails;
  }
}
