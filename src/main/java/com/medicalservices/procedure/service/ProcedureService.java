package com.medicalservices.procedure.service;

import com.medicalservices.procedure.model.Procedure;
import com.medicalservices.serviceprovider.model.ServiceProvider;
import com.medicalservices.serviceprovider.model.ServiceProviderDto;
import com.medicalservices.procedure.repository.ProcedureRepository;
import com.medicalservices.serviceprovider.repository.ServiceProviderRepository;
import com.medicalservices.serviceprovider.service.ServiceProviderService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ProcedureService {

  private ProcedureRepository procedureRepository;
  private ServiceProviderService serviceProviderService;
  private ServiceProviderRepository serviceProviderRepository;

  public List<ServiceProviderDto> getAllProcedures() {
    return serviceProviderService.getAllServiceProvider();

  }

  public Procedure getProcedureById(Long procedureId) {
    return procedureRepository.findById(procedureId).orElseThrow(() ->
      new HttpClientErrorException(HttpStatus.NOT_FOUND));
  }

  public Procedure patchProcedureById(Long procedureId, Procedure patchProcedure) {
    Procedure updatedProcedure = procedureRepository.findById(procedureId).map((procedure) -> procedure.toBuilder()
      .procedureName(Optional.ofNullable(patchProcedure.getProcedureName()).orElse(procedure.getProcedureName()))
      .description(Optional.ofNullable(patchProcedure.getDescription()).orElse(procedure.getDescription()))
      .price(patchProcedure.getPrice() != 0.0
        ? Optional.of(patchProcedure.getPrice()).get()
        : procedure.getPrice())
      .build()
    ).orElseThrow(
      () -> new HttpClientErrorException(HttpStatus.NOT_FOUND, "The procedure with provided ID not found.")
    );

    procedureRepository.save(updatedProcedure);
    return updatedProcedure;
  }

  private void addServiceProviderToProcedure(Long procedureId, Long serviceProviderId) {
    Procedure procedure = procedureRepository.findById(procedureId)
      .orElseThrow(() -> new HttpServerErrorException(HttpStatus.NOT_FOUND));
    ServiceProvider serviceProvider = serviceProviderRepository.findById(serviceProviderId)
      .orElseThrow(() -> new HttpServerErrorException(HttpStatus.NOT_FOUND));

    procedure.setServiceProvider(serviceProvider);
  }

  public Procedure createProcedure(Procedure procedure) {
    procedureRepository.save(procedure);
    addServiceProviderToProcedure(procedure.getId(), 1L);
    procedureRepository.save(procedure);
    return procedure;
  }

  public void deleteProcedureById(Long procedureId) {
    procedureRepository.deleteById(procedureId);
  }
}
