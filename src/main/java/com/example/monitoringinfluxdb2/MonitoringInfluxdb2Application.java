package com.example.monitoringinfluxdb2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;

@Configuration
@SpringBootApplication
public class MonitoringInfluxdb2Application {

  public static void main(String[] args) {
    SpringApplication.run(MonitoringInfluxdb2Application.class, args);
  }

}
