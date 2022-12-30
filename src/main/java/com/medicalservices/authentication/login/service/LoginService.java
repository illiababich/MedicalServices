package com.medicalservices.authentication.login.service;

import com.medicalservices.authentication.login.model.LoginRequest;
import com.medicalservices.security.jwt.service.TokenService;
import com.medicalservices.serviceprovider.model.ServiceProvider;
import com.medicalservices.serviceprovider.repository.ServiceProviderRepository;
import com.medicalservices.user.model.User;
import com.medicalservices.user.repository.UserRepository;
import com.medicalservices.authentication.login.model.LoginResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.HttpClientErrorException;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class LoginService {
  private final UserRepository userRepository;
  private final ServiceProviderRepository serviceProviderRepository;
  private final BCryptPasswordEncoder passwordEncoder;
  private final TokenService tokenService;
  private final AuthenticationManager authenticationManager;

  public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginBody) {
    try {
      Optional<User> optionalUser = userRepository.findByEmail(loginBody.getEmail());
      Optional<ServiceProvider> optionalServiceProvider = serviceProviderRepository.findByEmail(loginBody.getEmail());

      try {
        if (passwordEncoder.matches(loginBody.getPassword(), optionalUser.get().getPassword())) {
          String lastCheckupDate =
            Arrays.stream(optionalUser.get().getLastCheckupDate().toString().split(" ")).toList().get(0);
          LoginResponse loginResponse =
            new LoginResponse(lastCheckupDate, manageLogin(loginBody.getEmail(), loginBody.getPassword()));
          return new ResponseEntity<>(loginResponse, HttpStatus.OK);
        }
      } catch (Exception ignored) {
        // no handling
      }

      try {
        if (passwordEncoder.matches(loginBody.getPassword(), optionalServiceProvider.get().getPassword())) {
          return new ResponseEntity<>(
            new LoginResponse(manageLogin(loginBody.getEmail(), loginBody.getPassword())), HttpStatus.OK);
        }
      } catch (Exception ignored) {
        // no handling
      }

      throw new HttpClientErrorException(HttpStatus.UNAUTHORIZED, "Provided password is invalid");
    } catch (NullPointerException | NoSuchElementException exception) {
      log.info("Something went wrong");
    }
    return null;
  }

  private String manageLogin(String email, String password) {
    try {
      return tokenService.generateToken(authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(email, password)));
    } catch (BadCredentialsException exception) {
      log.info("Something went wrong");
    }
    return email;
  }
}
