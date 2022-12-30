package com.medicalservices.appointment.controller;

import com.medicalservices.appointment.model.Appointment;
import com.medicalservices.appointment.model.AppointmentRequest;
import com.medicalservices.appointment.service.AppointmentService;
import com.medicalservices.appointment.model.AppointmentDto;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping("/appointments")
@PreAuthorize("hasAnyAuthority('SCOPE_PATIENT', 'SCOPE_SERVICE_PROVIDER')")
public class AppointmentController {

  private final AppointmentService appointmentService;

  @GetMapping
  public List<AppointmentDto> getAvailableAppointments(@RequestParam Optional<Boolean> available) {
    return available.map((x) -> x
        ? appointmentService.getAvailableAppointments()
        : appointmentService.getUnavailableAppointments())
      .orElse(appointmentService.getAllAppointments());
  }

  @GetMapping(path = "/{appointmentId}")
  public Optional<Appointment> getAppointmentById(@PathVariable Long appointmentId) {
    return appointmentService.getAppointmentById(appointmentId);
  }

  @PostMapping(consumes = "application/json")
  @ResponseStatus(HttpStatus.OK)
  public Appointment createAppointment(@RequestBody AppointmentRequest appointment,
    Authentication authentication) {
    String userEmail = authentication.getName();
    return appointmentService.createAppointment(appointment, userEmail);
  }

  @DeleteMapping(path = "/{appointmentId}")
  @ResponseStatus(HttpStatus.OK)
  public void deleteAppointmentById(@PathVariable Long appointmentId) {
    appointmentService.deleteAppointmentById(appointmentId);
  }

  @PatchMapping(path = "/{appointmentId}", consumes = "application/json")
  public Appointment patchAppointment(@PathVariable Long appointmentId,
    @RequestBody AppointmentRequest patchAppointment) {
    return appointmentService.patchAppointmentById(appointmentId, patchAppointment);
  }

  @GetMapping(path = "/user")
  public Collection<AppointmentDto> getUserAppointments(Authentication authentication) {
    return appointmentService.getUserAppointments(authentication.getName());
  }
}
