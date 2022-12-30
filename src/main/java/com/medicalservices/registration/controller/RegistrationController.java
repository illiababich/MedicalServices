package com.medicalservices.registration.controller;

import com.medicalservices.registration.model.ServiceProviderRequestRegistration;
import com.medicalservices.registration.model.UserRegistrationRequest;
import com.medicalservices.registration.service.RegistrationService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping(path = "/registration", produces = "application/json")
public class RegistrationController {
  private RegistrationService registrationService;

  @PostMapping(path = "/user", consumes = "application/json")
  @ResponseStatus(HttpStatus.CREATED)
  public String registerUser(@RequestBody UserRegistrationRequest request) {
    return registrationService.registerUser(request);
  }

  @PostMapping(path = "/service_provider", consumes = "application/json")
  @ResponseStatus(HttpStatus.CREATED)
  public String registerServiceProvider(@RequestBody ServiceProviderRequestRegistration request) {
    return registrationService.registerServiceProvider(request);
  }
}
