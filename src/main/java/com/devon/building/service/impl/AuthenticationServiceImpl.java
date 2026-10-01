package com.devon.building.service.impl;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.StringJoiner;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.devon.building.constant.SystemConstant;
import com.devon.building.entity.InvalidatedToken;
import com.devon.building.entity.User;
import com.devon.building.exception.InvalidRequestException;
import com.devon.building.model.request.AuthenticationRequest;
import com.devon.building.model.request.ExchangeTokenRequest;
import com.devon.building.model.request.IntrospectRequest;
import com.devon.building.model.request.LogoutRequest;
import com.devon.building.model.request.RefreshRequest;
import com.devon.building.model.response.AuthenticationResponse;
import com.devon.building.model.response.IntrospectResponse;
import com.devon.building.repository.InvalidatedTokenRepository;
import com.devon.building.repository.UserRepository;
import com.devon.building.repository.httpclient.OutboundIdentityClient;
import com.devon.building.repository.httpclient.OutboundUserClient;
import com.devon.building.service.AuthenticationService;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.Payload;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationServiceImpl implements AuthenticationService {

    UserRepository userRepository;
    InvalidatedTokenRepository invalidatedTokenRepository;
    OutboundIdentityClient outboundIdentityClient;
    OutboundUserClient outboundUserClient;
    PasswordEncoder passwordEncoder;

    @NonFinal
    @Value("${jwt.signerKey}")
    String signerKey;

    @NonFinal
    @Value("${jwt.valid-duration:3600}")
    long validDuration;

    @NonFinal
    @Value("${jwt.refreshable-duration:86400}")
    long refreshableDuration;

    @NonFinal
    @Value("${outbound.identity.client-id}")
    String clientId;

    @NonFinal
    @Value("${outbound.identity.client-secret}")
    String clientSecret;

    @NonFinal
    @Value("${outbound.identity.redirect-uri}")
    String redirectUri;

    static final String GRANT_TYPE = "authorization_code";

    @Override
    public IntrospectResponse introspect(IntrospectRequest request) throws JOSEException, ParseException {
        boolean isValid = true;
        try {
            verifyToken(request.getToken(), false);
        } catch (Exception e) {
            isValid = false;
        }
        return IntrospectResponse.builder().valid(isValid).build();
    }

    @Override
    public AuthenticationResponse outboundAuthenticate(String code) {
        var response = outboundIdentityClient.exchangeToken(ExchangeTokenRequest.builder()
                .code(code)
                .clientId(clientId)
                .clientSecret(clientSecret)
                .redirectUri(redirectUri)
                .grantType(GRANT_TYPE)
                .build());

        var userInfo = outboundUserClient.getUserInfo("json", response.getAccessToken());

        String googleId = userInfo.getId();
        User user = null;
        if (googleId != null && !googleId.isBlank()) {
            user = userRepository.findByGoogleAccountId(googleId);
        }
        if (user == null && userInfo.getEmail() != null && !userInfo.getEmail().isBlank()) {
            user = userRepository.findByEmail(userInfo.getEmail());
            if (user == null) {
                user = userRepository.findByUserName(userInfo.getEmail());
            }
        }
        if (user == null) {
            user = new User();
            user.setUserName(userInfo.getEmail());
            user.setEmail(userInfo.getEmail());
            user.setGoogleAccountId(googleId);
            user.setFullName(userInfo.getGivenName() + " " + userInfo.getFamilyName());
            user.setUserRole(SystemConstant.USER_ROLE);
            user.setActive(true);
            user.setEncrytedPassword(passwordEncoder.encode(UUID.randomUUID().toString()));
            userRepository.save(user);
        } else if (user.getGoogleAccountId() == null && googleId != null && !googleId.isBlank()) {
            user.setGoogleAccountId(googleId);
            if (user.getEmail() == null || user.getEmail().isBlank()) {
                user.setEmail(userInfo.getEmail());
            }
            userRepository.save(user);
        }

        if (Boolean.FALSE.equals(user.getActive())) {
            throw new InvalidRequestException("Account is inactive");
        }

        return AuthenticationResponse.builder().token(generateToken(user)).build();
    }

    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        User user = userRepository.findByUserNameAndActiveTrue(request.getUsername());
        if (user == null) {
            throw new InvalidRequestException("Account does not exist or is inactive");
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getEncrytedPassword())) {
            throw new InvalidRequestException("Unauthenticated");
        }

        return AuthenticationResponse.builder()
                .token(generateToken(user))
                .authenticated(true)
                .build();
    }

    @Override
    public void logout(LogoutRequest request) throws ParseException, JOSEException {
        try {
            SignedJWT signedToken = verifyToken(request.getToken(), true);
            String jwtId = signedToken.getJWTClaimsSet().getJWTID();
            Date expiryTime = signedToken.getJWTClaimsSet().getExpirationTime();

            InvalidatedToken invalidatedToken = new InvalidatedToken();
            invalidatedToken.setId(jwtId);
            invalidatedToken.setExpiryTime(expiryTime);
            invalidatedTokenRepository.save(invalidatedToken);
        } catch (Exception exception) {
            log.debug("Logout received an already expired or invalid token");
        }
    }

    @Override
    public AuthenticationResponse refreshToken(RefreshRequest request) throws ParseException, JOSEException {
        SignedJWT signedJWT = verifyToken(request.getToken(), true);

        InvalidatedToken invalidatedToken = new InvalidatedToken();
        invalidatedToken.setId(signedJWT.getJWTClaimsSet().getJWTID());
        invalidatedToken.setExpiryTime(signedJWT.getJWTClaimsSet().getExpirationTime());
        invalidatedTokenRepository.save(invalidatedToken);

        String username = signedJWT.getJWTClaimsSet().getSubject();
        User user = userRepository.findByUserNameAndActiveTrue(username);
        if (user == null) {
            throw new InvalidRequestException("Account does not exist or is inactive");
        }

        return AuthenticationResponse.builder()
                .token(generateToken(user))
                .authenticated(true)
                .build();
    }

    private String generateToken(User user) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getUserName())
                .issuer("devon.com")
                .issueTime(new Date())
                .expirationTime(new Date(Instant.now().plus(validDuration, ChronoUnit.SECONDS).toEpochMilli()))
                .jwtID(UUID.randomUUID().toString())
                .claim("scope", buildScope(user))
                .build();

        JWSObject jwsObject = new JWSObject(header, new Payload(jwtClaimsSet.toJSONObject()));
        try {
            jwsObject.sign(new MACSigner(signerKey.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            log.error("Cannot create token", e);
            throw new IllegalStateException("Cannot create token", e);
        }
    }

    private SignedJWT verifyToken(String token, boolean isRefresh) throws JOSEException, ParseException {
        JWSVerifier verifier = new MACVerifier(signerKey.getBytes());
        SignedJWT signedJWT = SignedJWT.parse(token);

        Date expiryTime = isRefresh
                ? new Date(signedJWT.getJWTClaimsSet().getIssueTime().toInstant()
                        .plus(refreshableDuration, ChronoUnit.SECONDS).toEpochMilli())
                : signedJWT.getJWTClaimsSet().getExpirationTime();

        boolean verified = signedJWT.verify(verifier);
        if (!(verified && expiryTime.after(new Date()))) {
            throw new InvalidRequestException("Unauthenticated");
        }

        if (invalidatedTokenRepository.existsById(signedJWT.getJWTClaimsSet().getJWTID())) {
            throw new InvalidRequestException("Unauthenticated");
        }

        return signedJWT;
    }

    private String buildScope(User user) {
        StringJoiner stringJoiner = new StringJoiner(" ");
        if (user.getUserRole() != null && !user.getUserRole().isBlank()) {
            String role = user.getUserRole();
            if (!role.startsWith("ROLE_")) {
                role = "ROLE_" + role;
            }
            stringJoiner.add(role);
        }
        return stringJoiner.toString();
    }
}
