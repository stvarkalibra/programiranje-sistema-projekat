package org.example.finansije;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class FinansijeApplication {

    public static void main(String[] args) {
        SpringApplication.run(FinansijeApplication.class, args);
    }

}
