package com.myproject.firm.client;

import com.myproject.firm.client.config.AuthErrorDecoder;
import com.myproject.firm.client.request.GetTokenRequest;
import com.myproject.firm.client.response.GetTokenResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "ms-auth",
    url = "${api.auth.url}",
    configuration = AuthErrorDecoder.class)
public interface AuthClient {

  @PostMapping(value ="/realms/Firm/protocol/openid-connect/token", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
  GetTokenResponse getToken(@RequestBody GetTokenRequest request);

}
