package com.medicalservices.serviceprovider.controller;

import com.medicalservices.serviceprovider.model.ServiceProvider;
import com.medicalservices.serviceprovider.model.ServiceProviderDto;
import com.medicalservices.serviceprovider.service.ServiceProviderService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/service_providers")
@PreAuthorize("hasAnyAuthority('SCOPE_ADMIN', 'SCOPE_MANAGER')")
public class ServiceProviderController {
  private final ServiceProviderService serviceProviderService;

  @GetMapping("/{serviceProviderId}")
  public ServiceProvider getServiceProviderById(@PathVariable("serviceProviderId") Long serviceProviderId) {
    return serviceProviderService.getServiceProviderById(serviceProviderId);
  }

  @GetMapping()
  @ResponseStatus(HttpStatus.OK)
  public List<ServiceProviderDto> getAllServiceProvider() {
    return serviceProviderService.getAllServiceProvider();
  }

  @DeleteMapping("/{serviceProviderId}")
  public ResponseEntity<String> deleteServiceProviderById(@PathVariable("serviceProviderId") Long serviceProviderId) {
    return serviceProviderService.deleteServiceProviderById(serviceProviderId);
  }

  @PatchMapping(path = "/{serviceProviderId}", consumes = "application/json")
  public ServiceProvider patchServiceProvider(@PathVariable("serviceProviderId") Long serviceProviderId,
    @RequestBody ServiceProvider serviceProviderPatch) {
    return serviceProviderService.patchServiceProviderById(serviceProviderId, serviceProviderPatch);
  }
}
