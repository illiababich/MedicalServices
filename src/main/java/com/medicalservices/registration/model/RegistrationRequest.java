package com.medicalservices.registration.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@EqualsAndHashCode
@ToString
@NoArgsConstructor
public class RegistrationRequest {
  private String email;
  private String password;

  public RegistrationRequest(String email, String password) {
    this.email = email;
    this.password = password;
  }
}
