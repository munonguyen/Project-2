package com.devon.building.service.impl;

import com.devon.building.constant.SystemConstant;
import com.devon.building.entity.User;
import com.devon.building.repository.UserRepository;
import com.devon.building.utils.OAuth2PictureFetcher;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true )
public class CustomOAuth2UserService extends DefaultOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final BCryptPasswordEncoder passwordEncoder;
    private final OAuth2PictureFetcher oAuth2PictureFetcher;
    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser( OAuth2UserRequest userRequest) throws OAuth2AuthenticationException{
        OAuth2User oAuth2User = super.loadUser(userRequest);
        log.info("Attribute: {}",oAuth2User.getAttributes());

        String id = oAuth2User.getAttributes().get("id").toString();
        String login = oAuth2User.getAttributes().get("login").toString();
        User user = null;
        try{
            user = userRepository.findByUserName(login);
            if(user == null){
                user = createGithubUser(oAuth2User);
            }
            if(Boolean.FALSE.equals(user.getActive())){
                log.warn("Login attempt fot inactive user: {}",id);
                throw new OAuth2AuthenticationException("Account must be active");
            }
        }catch(OAuth2AuthenticationException e){
            throw e;
        }catch(Exception e){
            log.error("Failed to load or create user: {}",id);
            throw new OAuth2AuthenticationException(new OAuth2Error("server error"),"Error persisting user: "+e.getMessage());
        }
        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(user.getUserRole());
        return new DefaultOAuth2User(Collections.singleton(grantedAuthority),oAuth2User.getAttributes(),"login");
    }

    private User createGithubUser(OAuth2User oAuth2User){
        User user = new User();
        user.setUserName(oAuth2User.getAttributes().get("login").toString());
        user.setActive(true);
        user.setUserRole(SystemConstant.USER_ROLE);
        user.setFullName(oAuth2User.getAttributes().get("name").toString());
        user.setGithubAccountId(oAuth2User.getAttributes().get("id").toString());
        user.setEncrytedPassword(passwordEncoder.encode(UUID.randomUUID().toString()));
        user.setImage(oAuth2PictureFetcher.fetchGoogleProfilePicture(oAuth2User.getAttributes().get("avatar_url").toString()));
        return userRepository.save(user);


    }






    

}
