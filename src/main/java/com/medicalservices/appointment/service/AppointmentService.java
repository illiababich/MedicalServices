package com.medicalservices.appointment.service;

import com.medicalservices.appointment.mapper.AppointmentMapper;
import com.medicalservices.appointment.model.Appointment;
import com.medicalservices.appointment.model.AppointmentDto;
import com.medicalservices.appointment.model.AppointmentRequest;
import com.medicalservices.appointment.repository.AppointmentRepository;
import com.medicalservices.procedure.model.Procedure;
import com.medicalservices.procedure.repository.ProcedureRepository;
import com.medicalservices.timeslot.model.Timeslot;
import com.medicalservices.timeslot.repository.TimeslotRepository;
import com.medicalservices.user.model.User;
import com.medicalservices.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AppointmentService {

  private AppointmentRepository appointmentRepository;
  private TimeslotRepository timeslotRepository;
  private UserRepository userRepository;
  private ProcedureRepository procedureRepository;

  public List<AppointmentDto> getAllAppointments() {
    return appointmentRepository.findAll().stream().map(AppointmentMapper::toDto).collect(Collectors.toList());
  }

  public List<AppointmentDto> getAvailableAppointments() {
    return appointmentRepository.findAvailableAppointments().stream()
      .map(AppointmentMapper::toDto).collect(Collectors.toList());
  }

  public List<AppointmentDto> getUnavailableAppointments() {
    return appointmentRepository.findUnavailableAppointments().stream()
      .map(AppointmentMapper::toDto).collect(Collectors.toList());
  }

  public Optional<Appointment> getAppointmentById(Long appointmentId) {
    return Optional.ofNullable(appointmentRepository.findById(appointmentId).orElseThrow(
      () -> new HttpServerErrorException(HttpStatus.NOT_FOUND)));
  }

  public Appointment createAppointment(AppointmentRequest appointmentRequest,
    String userEmail) {
    Appointment appointment;

    Timeslot timeslot = timeslotRepository.findById(appointmentRequest.getTimeslotId())
      .orElseThrow(() -> new HttpServerErrorException(HttpStatus.NOT_FOUND));
    if (userEmail != null) {
      User user = userRepository.findByEmail(userEmail)
        .orElseThrow(() -> new HttpServerErrorException(HttpStatus.NOT_FOUND));

      appointment = new Appointment(user, timeslot, appointmentRequest.getComment());
    } else {
      appointment = new Appointment(timeslot, appointmentRequest.getComment());
    }

    appointmentRepository.save(appointment);

    appointmentRequest.getProcedures().forEach(procedure ->
      addProceduresToAppointment(appointment.getId(), procedure.getId()));

    return appointment;
  }

  private void addProceduresToAppointment(long appointmentId, long procedureId) {
    Appointment appointment = appointmentRepository.findById(appointmentId)
      .orElseThrow(() -> new HttpServerErrorException(HttpStatus.NOT_FOUND));
    Procedure procedure = procedureRepository.findById(procedureId)
      .orElseThrow(() -> new HttpServerErrorException(HttpStatus.NOT_FOUND));

    appointment.getProcedures().add(procedure);
    appointmentRepository.save(appointment);
  }

  public void deleteAppointmentById(Long appointmentId) {
    appointmentRepository.delete(appointmentRepository.findById(appointmentId).orElseThrow(
      () -> new HttpServerErrorException(HttpStatus.NOT_FOUND)));
  }

  public Appointment patchAppointmentById(Long appointmentId, AppointmentRequest patchAppointment) {
    Appointment updateAppointment = appointmentRepository.findById(appointmentId)
      .map((appointment) -> appointment.toBuilder()
        .comment(Optional.of(patchAppointment.getComment()).orElse(appointment.getComment()))
        .timeslot((null == patchAppointment.getTimeslotId())
          ? appointment.getTimeslot()
          : Optional.of(timeslotRepository.findById(patchAppointment.getTimeslotId()).get())
          .orElseThrow(() -> new HttpServerErrorException(HttpStatus.NOT_FOUND)))
        .build()
      ).orElseThrow(
        () -> new HttpClientErrorException(HttpStatus.NOT_FOUND, "The Appointment with provided ID not found.")
      );

    appointmentRepository.save(updateAppointment);

    return updateAppointment;
  }

  public Collection<AppointmentDto> getUserAppointments(String email) {
    return appointmentRepository.findUserAppointments(email).stream().map(AppointmentMapper::toDto)
      .collect(Collectors.toList());

  }
}
