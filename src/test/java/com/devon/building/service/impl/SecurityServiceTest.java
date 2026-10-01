package com.devon.building.service.impl;

import com.devon.building.constant.SystemConstant;
import com.devon.building.entity.User;
import com.devon.building.exception.InvalidRequestException;
import com.devon.building.model.dto.UserRegisterDTO;
import com.devon.building.model.request.AuthenticationRequest;
import com.devon.building.repository.InvalidatedTokenRepository;
import com.devon.building.repository.UserRepository;
import com.devon.building.repository.httpclient.OutboundIdentityClient;
import com.devon.building.repository.httpclient.OutboundUserClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SecurityServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private InvalidatedTokenRepository invalidatedTokenRepository;
    @Mock
    private OutboundIdentityClient outboundIdentityClient;
    @Mock
    private OutboundUserClient outboundUserClient;
    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    void publicRegistrationAlwaysCreatesRegularUser() {
        UserServiceImpl service = new UserServiceImpl(userRepository, passwordEncoder);
        UserRegisterDTO request = UserRegisterDTO.builder()
                .fullname("Normal User")
                .userName("normal_user")
                .phoneNumber("0123456789")
                .password("secret123")
                .retypePassword("secret123")
                .build();

        when(userRepository.findByUserName("normal_user")).thenReturn(null);
        when(passwordEncoder.encode("secret123")).thenReturn("encoded-password");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User created = service.register(request);

        assertEquals(SystemConstant.USER_ROLE, created.getUserRole());
        assertEquals(Boolean.TRUE, created.getActive());

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(captor.capture());
        assertEquals(SystemConstant.USER_ROLE, captor.getValue().getUserRole());
    }

    @Test
    void inactiveUserCannotAuthenticateWithJwtLogin() {
        AuthenticationServiceImpl service = new AuthenticationServiceImpl(
                userRepository,
                invalidatedTokenRepository,
                outboundIdentityClient,
                outboundUserClient,
                passwordEncoder);

        when(userRepository.findByUserNameAndActiveTrue("disabled_user")).thenReturn(null);

        AuthenticationRequest request = AuthenticationRequest.builder()
                .username("disabled_user")
                .password("password")
                .build();

        assertThrows(InvalidRequestException.class, () -> service.authenticate(request));
        verify(passwordEncoder, never()).matches(any(), any());
    }
}
