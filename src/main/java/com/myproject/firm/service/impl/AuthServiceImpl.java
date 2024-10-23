package com.myproject.firm.service.impl;

import com.myproject.firm.client.AuthClient;
import com.myproject.firm.client.request.GetTokenRequest;
import com.myproject.firm.client.response.GetTokenResponse;
import com.myproject.firm.config.auth.AuthConfigProperties;
import com.myproject.firm.dto.request.ClientCredentialsRequest;
import com.myproject.firm.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

  private final AuthClient authClient;
  private final AuthConfigProperties authConfigProperties;

  @Override
  public GetTokenResponse getToken(ClientCredentialsRequest request) {
    GetTokenRequest getTokenRequest = buildGetTokenRequest(request);
    return authClient.getToken(getTokenRequest);
  }

  private GetTokenRequest buildGetTokenRequest(ClientCredentialsRequest request) {
    return GetTokenRequest.builder()
        .client_id(authConfigProperties.getClientId())
        .grant_type(authConfigProperties.getGrantType())
        .password(request.getPassword())
        .username(request.getUsername())
        .build();
  }


}
