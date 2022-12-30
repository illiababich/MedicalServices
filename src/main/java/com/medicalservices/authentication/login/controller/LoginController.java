package com.medicalservices.authentication.login.controller;

import com.medicalservices.authentication.login.model.LoginRequest;
import com.medicalservices.authentication.login.model.LoginResponse;
import com.medicalservices.authentication.login.service.LoginService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/login", produces = "application/json")
@AllArgsConstructor
public class LoginController {
  public final LoginService loginService;

  @PostMapping()
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginBody) {
    return loginService.login(loginBody);
  }
}
