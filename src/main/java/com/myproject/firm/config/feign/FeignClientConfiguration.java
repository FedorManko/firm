package com.myproject.firm.config.feign;

import com.myproject.firm.client.AuthClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(clients = {AuthClient.class})
public class FeignClientConfiguration {

}
