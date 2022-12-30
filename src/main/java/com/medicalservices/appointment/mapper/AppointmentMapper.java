package com.medicalservices.appointment.mapper;

import com.medicalservices.appointment.model.Appointment;
import com.medicalservices.procedure.mapper.ProcedureMapper;
import com.medicalservices.timeslot.mapper.TimeslotMapper;
import com.medicalservices.appointment.model.AppointmentDto;
import java.util.stream.Collectors;

public class AppointmentMapper {
  public static AppointmentDto toDto(Appointment appointment) {

    return AppointmentDto.builder()
      .id(appointment.getId())
      .timeslot(TimeslotMapper.toDto(appointment.getTimeslot()))
      .procedures(appointment.getProcedures().stream()
        .map(ProcedureMapper::toDto).collect(Collectors.toList()))
      .comment(appointment.getComment())
      .build();

  }
}
