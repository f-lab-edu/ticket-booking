package org.project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class TicketBookingApplication {

  public static void main(String[] args) {
    SpringApplication.run(TicketBookingApplication.class, args);
  }
}
