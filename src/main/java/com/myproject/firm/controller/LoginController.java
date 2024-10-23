package com.myproject.firm.controller;

import com.myproject.firm.client.response.GetTokenResponse;
import com.myproject.firm.dto.request.ClientCredentialsRequest;
import com.myproject.firm.service.AuthService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class LoginController {

  private final AuthService authService;

  @PostMapping("/token")
  public ResponseEntity<GetTokenResponse> getToken(@RequestBody ClientCredentialsRequest request) {
    GetTokenResponse token = authService.getToken(request);
    return ResponseEntity.ok(token);
  }

}
