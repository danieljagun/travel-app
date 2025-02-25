package com.travel.app;

import com.travel.app.repository.TripRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@ConfigurationPropertiesScan
@EnableMongoRepositories(basePackageClasses = TripRepository.class)
public class TravelAppApplication {

  public static void main(String[] args) {
    SpringApplication.run(TravelAppApplication.class, args);
  }
}
