package org.example.rezervacije;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class RezervacijeApplication {

    public static void main(String[] args) {
        SpringApplication.run(RezervacijeApplication.class, args);
    }

}
