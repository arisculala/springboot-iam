package com.users.management;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@OpenAPIDefinition(info = @Info(title = "User Management API", version = "0.0.1", description = "API for managing users in the system"))
@SpringBootApplication(scanBasePackages = { "com.users.management", "com.generic.utility.handlers" })
public class TradingUsersManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(TradingUsersManagementApplication.class, args);
    }
}
