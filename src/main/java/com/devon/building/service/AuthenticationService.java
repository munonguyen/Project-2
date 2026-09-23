package com.devon.building.service;

import java.text.ParseException;

import com.devon.building.model.request.AuthenticationRequest;
import com.devon.building.model.request.IntrospectRequest;
import com.devon.building.model.request.LogoutRequest;
import com.devon.building.model.request.RefreshRequest;
import com.devon.building.model.response.AuthenticationResponse;
import com.devon.building.model.response.IntrospectResponse;
import com.nimbusds.jose.JOSEException;

public interface AuthenticationService {
    IntrospectResponse introspect(IntrospectRequest request) throws JOSEException, ParseException;

    AuthenticationResponse outboundAuthenticate(String code);

    AuthenticationResponse authenticate(AuthenticationRequest request);

    void logout(LogoutRequest request) throws ParseException, JOSEException;

    AuthenticationResponse refreshToken(RefreshRequest request) throws ParseException, JOSEException;
}
