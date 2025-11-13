package com.example.sparta.hub_route_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(scanBasePackages = {
	"com.example.sparta.hub_route_service",
	"com.example.sparta.common"
})
@EnableFeignClients
public class HubRouteServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(HubRouteServiceApplication.class, args);
	}

}
