package com.medicalservices.registration.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import java.util.Date;

@Getter
@Setter
@ToString
public class UserRegistrationRequest extends RegistrationRequest {
  private String firstName;
  private String lastName;
  private Date birthDate;

  public UserRegistrationRequest(String email, String password, String firstName, String lastName, Date birthDate) {
    super(email, password);
    this.firstName = firstName;
    this.lastName = lastName;
    this.birthDate = birthDate;
  }
}
