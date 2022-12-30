package com.medicalservices.registration.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ServiceProviderRequestRegistration extends RegistrationRequest {
  private String companyName;
  private String phoneNumber;

  public ServiceProviderRequestRegistration(String email, String password, String companyName,
    String phoneNumber) {
    super(email, password);
    this.companyName = companyName;
    this.phoneNumber = phoneNumber;
  }
}
