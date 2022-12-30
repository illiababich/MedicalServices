package com.medicalservices;

import com.medicalservices.security.config.RsaKeyProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties(RsaKeyProperties.class)
@SpringBootApplication
public class MedicalServicesApplication {
  public static void main(String[] args) {
    SpringApplication.run(MedicalServicesApplication.class, args);
  }
}
