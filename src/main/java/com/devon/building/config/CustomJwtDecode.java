package com.devon.building.config;

import java.nio.charset.StandardCharsets;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Component;

import com.devon.building.repository.InvalidatedTokenRepository;

@Component
public class CustomJwtDecode implements JwtDecoder {

    private final InvalidatedTokenRepository invalidatedTokenRepository;
    private final NimbusJwtDecoder delegate;

    public CustomJwtDecode(
            InvalidatedTokenRepository invalidatedTokenRepository,
            @Value("${jwt.signerKey}") String signerKey) {
        this.invalidatedTokenRepository = invalidatedTokenRepository;
        SecretKeySpec secretKeySpec = new SecretKeySpec(
                signerKey.getBytes(StandardCharsets.UTF_8),
                "HmacSHA512");
        this.delegate = NimbusJwtDecoder.withSecretKey(secretKeySpec)
                .macAlgorithm(MacAlgorithm.HS512)
                .build();
    }

    @Override
    public Jwt decode(String token) throws JwtException {
        Jwt jwt = delegate.decode(token);
        String jwtId = jwt.getId();
        if (jwtId != null && invalidatedTokenRepository.existsById(jwtId)) {
            throw new JwtException("Token has been invalidated");
        }
        return jwt;
    }
}
