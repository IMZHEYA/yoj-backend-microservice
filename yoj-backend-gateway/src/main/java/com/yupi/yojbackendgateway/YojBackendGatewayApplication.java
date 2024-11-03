package com.yupi.yojbackendgateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class YojBackendGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(YojBackendGatewayApplication.class, args);
    }

}
