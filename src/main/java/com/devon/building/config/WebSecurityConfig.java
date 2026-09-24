package com.devon.building.config;

import com.devon.building.constant.SystemConstant;
import com.devon.building.security.CustomSuccessHandler;
import com.devon.building.service.impl.CustomOid2UserService;
import com.devon.building.service.impl.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final UserDetailsServiceImpl userDetailsService;
    private final CustomJwtDecode customJwtDecoder;
    private final CustomOid2UserService customOid2UserService;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        grantedAuthoritiesConverter.setAuthorityPrefix("");

        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);
        return jwtAuthenticationConverter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, CustomOid2UserService customOid2UserService) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/**", "/oauth2/**", "/login/oauth2/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/users/login", "/api/users/register", "/users/login", "/users/register").permitAll()
                        .requestMatchers(HttpMethod.POST, "/contact").permitAll()
                        .requestMatchers("/admin/users/userImage").permitAll()
                        .requestMatchers("/admin/users/change-password/**").hasAnyRole(SystemConstant.STAFF, SystemConstant.MANAGER)
                        .requestMatchers("/admin/users/list", "/admin/users").hasRole(SystemConstant.MANAGER)
                        .requestMatchers("/admin/users/{userName}").hasAnyRole(SystemConstant.STAFF, SystemConstant.MANAGER)
                        .requestMatchers("/admin/users/**").hasRole(SystemConstant.MANAGER)
                        .requestMatchers(HttpMethod.PUT, "/api/buildings/assign").hasRole(SystemConstant.MANAGER)
                        .requestMatchers(HttpMethod.DELETE, "/api/buildings/**").hasRole(SystemConstant.MANAGER)
                        .requestMatchers(HttpMethod.DELETE, "/api/customers/**").hasRole(SystemConstant.MANAGER)
                        .requestMatchers(HttpMethod.PUT, "/api/customers/assign").hasRole(SystemConstant.MANAGER)
                        .requestMatchers(HttpMethod.DELETE, "/api/transactions/**").hasRole(SystemConstant.MANAGER)
                        .requestMatchers("/api/buildings/**", "/api/customers/**", "/api/transactions/**").hasAnyRole(SystemConstant.STAFF, SystemConstant.MANAGER)
                        .requestMatchers(HttpMethod.POST, "/users", "/api/users", "/users/**", "/api/users/**").hasRole(SystemConstant.MANAGER)
                        .requestMatchers(HttpMethod.DELETE, "/users/**", "/api/users/**").hasRole(SystemConstant.MANAGER)
                        .requestMatchers("/users/**", "/api/users/**").hasAnyRole(SystemConstant.STAFF, SystemConstant.MANAGER)
                        .requestMatchers("/admin/**").hasAnyRole(SystemConstant.STAFF, SystemConstant.MANAGER)
                        .anyRequest().permitAll())
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwtConfigurer -> jwtConfigurer
                                .decoder(customJwtDecoder)
                                .jwtAuthenticationConverter(jwtAuthenticationConverter())))
                .exceptionHandling(ex -> ex.accessDeniedPage("/403"))
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/login") // Xử lý POST /login
                        .successHandler(myAuthenticationSuccessHandler())
                        .failureUrl("/login?incorrectAccount")
                        .usernameParameter("userName")
                        .passwordParameter("password")
                        .permitAll())
                .oauth2Login(oauth2 -> oauth2
                        .loginPage("/login")
                        .userInfoEndpoint(userInfo -> userInfo.oidcUserService(customOid2UserService))
                        .successHandler(myAuthenticationSuccessHandler())
                        .failureUrl("/login?error"))
                .logout(logout -> logout
                        .invalidateHttpSession(true)
                        .logoutUrl("/admin/logout")
                        .logoutSuccessUrl("/")
                        .permitAll()
                ).sessionManagement(session -> session
                        .maximumSessions(1) //Chi cho phep 1 phien dang nhap cung 1 luc
                        .maxSessionsPreventsLogin(false)
                );

        return http.build();
    }

    @Bean
    public AuthenticationSuccessHandler myAuthenticationSuccessHandler() {
        return new CustomSuccessHandler();
    }
}
