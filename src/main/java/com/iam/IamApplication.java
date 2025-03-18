package com.iam;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@OpenAPIDefinition(info = @Info(title = "IAM Service API", version = "0.0.1", description = "API for Identity and Access Management"))
@SpringBootApplication(scanBasePackages = { "com.iam", "com.generic" })
public class IamApplication {
    public static void main(String[] args) {
        SpringApplication.run(IamApplication.class, args);
    }
}
