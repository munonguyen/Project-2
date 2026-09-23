package com.devon.building.utils;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

import java.util.Collections;
import java.util.List;

public class SecurityUtils {

    private SecurityUtils() {}

    public static String getCurrentUsername(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null){
            return null;
        }
        Object principal = authentication.getPrincipal();
        if(principal instanceof UserDetails userDetails){
            return userDetails.getUsername();
        }
        if(principal instanceof org.springframework.security.oauth2.core.user.OAuth2User oAuth2User){
         return oAuth2User.getName();
        }
        if(principal instanceof OidcUser oidcUser){
            return oidcUser.getEmail();
        }
        return null;
    }
    public static List<String> getAuthorities(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null){
            return Collections.emptyList();
        }
        return authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
    }
}
