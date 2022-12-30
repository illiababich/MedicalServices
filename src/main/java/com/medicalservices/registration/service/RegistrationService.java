package com.medicalservices.registration.service;

import com.medicalservices.serviceprovider.model.ServiceProvider;
import com.medicalservices.user.service.UserService;
import com.medicalservices.registration.model.ServiceProviderRequestRegistration;
import com.medicalservices.registration.model.UserRegistrationRequest;
import com.medicalservices.serviceprovider.service.ServiceProviderService;
import com.medicalservices.user.model.User;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@AllArgsConstructor
@Slf4j
public class RegistrationService {
  private final UserService userService;
  private final ServiceProviderService serviceProviderService;

  private boolean isEmailValid(String email) {
    Pattern pattern = Pattern.compile("@devbridge.com$");
    Matcher matcher = pattern.matcher(email);
    return matcher.find();
  }

  public String registerUser(UserRegistrationRequest request) {
    try {

      if (!isEmailValid(request.getEmail())) {
        throw new HttpClientErrorException(HttpStatus.UNAUTHORIZED,
          "Provided email is not valid. Your email should end with @devbridge.com");
      }

      Optional<User> user = Optional.of(
        new User(request.getEmail(), request.getPassword(), request.getFirstName(), request.getLastName(),
          request.getBirthDate()));

      userService.signUpUser(user.get());
      userService.registerDefaultUser(user.get());
      return user.get().toString();
    } catch (NullPointerException exception) {
      log.info("Something went wrong");
    }
    return null;
  }

  public String registerServiceProvider(ServiceProviderRequestRegistration request) {
    try {
      Optional<ServiceProvider> serviceProvider = Optional.of(
        new ServiceProvider(request.getEmail(), request.getPassword(), request.getCompanyName(),
          request.getPhoneNumber()));
      serviceProviderService.signUpServiceProvider(serviceProvider.get());
      return serviceProvider.get().toString();
    } catch (NullPointerException exception) {
      log.info("Something went wrong");
    }
    return null;
  }
}
