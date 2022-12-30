package com.medicalservices.serviceprovider.service;

import com.medicalservices.serviceprovider.mapper.ServiceProviderMapper;
import com.medicalservices.serviceprovider.model.ServiceProvider;
import com.medicalservices.serviceprovider.model.ServiceProviderDto;
import com.medicalservices.serviceprovider.repository.ServiceProviderRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ServiceProviderService {
  private final ServiceProviderRepository serviceProviderRepository;
  private final BCryptPasswordEncoder passwordEncoder;

  public void signUpServiceProvider(ServiceProvider serviceProvider) {
    Optional<ServiceProvider> optionalSP = serviceProviderRepository.findByEmail(serviceProvider.getEmail());

    if (optionalSP.isPresent()) {
      throw new IllegalStateException("Email provided is already taken");
    }

    String encodedPassword = passwordEncoder.encode(serviceProvider.getPassword());
    serviceProvider.setPassword(encodedPassword);

    serviceProviderRepository.save(serviceProvider);

    new ResponseEntity<>("Successfully created a service provider", HttpStatus.CREATED);
  }

  public ServiceProvider getServiceProviderById(Long id) {
    return serviceProviderRepository.findById(id).orElseThrow(() -> new HttpServerErrorException(HttpStatus.NOT_FOUND));
  }

  public List<ServiceProviderDto> getAllServiceProvider() {
    return serviceProviderRepository.findAll().stream().map(ServiceProviderMapper::toDto)
      .collect(Collectors.toList());
  }

  public ResponseEntity<String> deleteServiceProviderById(Long serviceProviderId) {
    try {
      serviceProviderRepository.deleteById(serviceProviderId);

      return new ResponseEntity<>("The Service Provider was deleted successfully.", HttpStatus.OK);
    } catch (NoSuchElementException exception) {
      return new ResponseEntity<>("The Service Provider with given ID not found.", HttpStatus.NOT_FOUND);
    }
  }

  public ServiceProvider patchServiceProviderById(Long serviceProviderId, ServiceProvider serviceProviderPatch) {
    ServiceProvider updateServiceProvider = serviceProviderRepository.findById(serviceProviderId)
      .map((serviceProvider) -> serviceProvider.toBuilder()
        .email(Optional.ofNullable(serviceProviderPatch.getEmail()).orElse(serviceProvider.getEmail()))
        .companyName(Optional.ofNullable(serviceProviderPatch.getCompanyName())
          .orElse(serviceProvider.getCompanyName()))
        .phoneNumber(Optional.ofNullable(serviceProviderPatch.getPhoneNumber())
          .orElse(serviceProvider.getPhoneNumber()))
        .password(null != serviceProviderPatch.getPassword()
          ? Optional.of(passwordEncoder.encode(serviceProviderPatch.getPassword())).get()
          : serviceProvider.getPassword())
        .enabled(Optional.ofNullable(serviceProviderPatch.getEnabled()).orElse(serviceProvider.getEnabled()))
        .build()
      ).orElseThrow(
        () -> new HttpClientErrorException(HttpStatus.NOT_FOUND, "The Service Provider with provided ID not found.")
      );

    serviceProviderRepository.save(updateServiceProvider);
    return updateServiceProvider;
  }
}
