package com.medicalservices.authentication.login.model;

import lombok.Getter;

@Getter
public class LoginResponse {
  private String lastCheckupDate;
  private final String bearer;

  public LoginResponse(String bearer) {
    this.bearer = bearer;
  }

  public LoginResponse(String lastCheckupDate, String bearer) {
    this.lastCheckupDate = lastCheckupDate;
    this.bearer = bearer;
  }
}
