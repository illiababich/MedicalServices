package com.medicalservices.timeslot.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Builder
@Data
@AllArgsConstructor
public class TimeslotDto {
  private Long id;
  private Timestamp startTime;
  private Timestamp endTime;
  private Long serviceProviderId;
  private String serviceProviderName;

  public String getStartTimeDate() {
    LocalDate date = startTime.toLocalDateTime().toLocalDate();
    return date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
  }
}
