package com.devon.building.config;

import java.text.ParseException;
import java.util.Objects;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Component;

import com.devon.building.model.request.IntrospectRequest;
import com.devon.building.service.AuthenticationService;
import com.nimbusds.jose.JOSEException;

@Component
public class CustomJwtDecode implements JwtDecoder {

    private final AuthenticationService authenticationService;
    private final String signerKey;
    private NimbusJwtDecoder nimbusJwtDecoder;

    public CustomJwtDecode(AuthenticationService authenticationService,
            @Value("${jwt.signerKey}") String signerKey) {
        this.authenticationService = authenticationService;
        this.signerKey = signerKey;
    }

    @Override
    public Jwt decode(String token) throws JwtException {
        try {
            var response = authenticationService.introspect(
                    IntrospectRequest.builder().token(token).build());
            if (!response.isValid()) {
                throw new JwtException("Token invalid");
            }
        } catch (JOSEException | ParseException e) {
            throw new JwtException("Token validation failed", e);
        }

        if (Objects.isNull(nimbusJwtDecoder)) {
            SecretKeySpec secretKeySpec = new SecretKeySpec(signerKey.getBytes(), "HS512");
            nimbusJwtDecoder = NimbusJwtDecoder.withSecretKey(secretKeySpec)
                    .macAlgorithm(MacAlgorithm.HS512)
                    .build();
        }

        return nimbusJwtDecoder.decode(token);
    }
}
