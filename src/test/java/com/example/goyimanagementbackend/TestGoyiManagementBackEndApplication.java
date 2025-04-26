package com.example.goyimanagementbackend;

import org.springframework.boot.SpringApplication;

public class TestGoyiManagementBackEndApplication {

    public static void main(String[] args) {
        SpringApplication.from(GoyiManagementBackEndApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
