package com.thoughtworks.airticketorder;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class AirTicketOrderApplication {

    public static void main(String[] args) {
        SpringApplication.run(AirTicketOrderApplication.class, args);
    }

}
