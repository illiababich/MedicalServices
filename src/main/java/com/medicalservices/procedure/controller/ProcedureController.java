package com.medicalservices.procedure.controller;

import com.medicalservices.procedure.model.Procedure;
import com.medicalservices.procedure.service.ProcedureService;
import com.medicalservices.serviceprovider.model.ServiceProviderDto;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.access.prepost.PreAuthorize;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping(path = "/procedures")
public class ProcedureController {
  private ProcedureService procedureService;

  @GetMapping
  public List<ServiceProviderDto> listAllProcedures() {
    return procedureService.getAllProcedures();
  }

  @GetMapping("/{procedureId}")
  @PreAuthorize("hasAnyAuthority('SCOPE_ADMIN', 'SCOPE_SERVICE_PROVIDER')")
  public Procedure getProcedureById(@PathVariable(value = "procedureId") Long procedureId) {
    return procedureService.getProcedureById(procedureId);
  }

  @PatchMapping(path = "/{procedureId}", consumes = "application/json")
  @PreAuthorize("hasAnyAuthority('SCOPE_ADMIN', 'SCOPE_SERVICE_PROVIDER')")
  public Procedure patchProcedureById(@PathVariable Long procedureId, @RequestBody Procedure procedure) {
    return procedureService.patchProcedureById(procedureId, procedure);
  }

  @PostMapping(consumes = "application/json")
  @PreAuthorize("hasAnyAuthority('SCOPE_ADMIN', 'SCOPE_SERVICE_PROVIDER')")
  public Procedure createProcedure(@RequestBody Procedure procedure) {
    return procedureService.createProcedure(procedure);
  }

  @DeleteMapping(path = "/{procedureId}")
  @PreAuthorize("hasAnyAuthority('SCOPE_ADMIN', 'SCOPE_SERVICE_PROVIDER')")
  @ResponseStatus(HttpStatus.OK)
  public void deleteProcedureById(@PathVariable Long procedureId) {
    procedureService.deleteProcedureById(procedureId);
  }
}
