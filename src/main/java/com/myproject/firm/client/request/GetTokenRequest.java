package com.myproject.firm.client.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetTokenRequest {

  private String grant_type;
  private String client_id;
  private String username;
  private String password;

}
