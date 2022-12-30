package com.medicalservices.appointment.model;

import com.medicalservices.procedure.model.Procedure;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import java.util.List;

@Data
@RequiredArgsConstructor
public class AppointmentRequest {
  private Long timeslotId;
  private String comment;
  private List<Procedure> procedures;
}
