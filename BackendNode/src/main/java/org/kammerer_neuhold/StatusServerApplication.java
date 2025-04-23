package org.kammerer_neuhold;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class StatusServerApplication {

    public static void main(final String[] args) {
        SpringApplication.run(StatusServerApplication.class, args);
    }
}
