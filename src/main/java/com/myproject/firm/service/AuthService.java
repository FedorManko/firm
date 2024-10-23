package com.myproject.firm.service;

import com.myproject.firm.client.response.GetTokenResponse;
import com.myproject.firm.dto.request.ClientCredentialsRequest;

public interface AuthService {

  GetTokenResponse getToken(ClientCredentialsRequest request);

}
