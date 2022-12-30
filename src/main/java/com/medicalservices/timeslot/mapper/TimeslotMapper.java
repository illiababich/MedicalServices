package com.medicalservices.timeslot.mapper;

import com.medicalservices.serviceprovider.model.ServiceProvider;
import com.medicalservices.timeslot.model.Timeslot;
import com.medicalservices.timeslot.model.TimeslotDto;
import java.util.Optional;

public class TimeslotMapper {

  public static TimeslotDto toDto(Timeslot timeslot) {
    Long serviceProviderId = Optional.ofNullable(timeslot.getServiceProvider())
      .map(ServiceProvider::getId).orElse(0L);
    String serviceProviderName = Optional
      .ofNullable(timeslot.getServiceProvider())
      .map(ServiceProvider::getCompanyName).orElse("");

    return TimeslotDto.builder()
      .id(timeslot.getId())
      .startTime(timeslot.getStartTime())
      .endTime(timeslot.getEndTime())
      .serviceProviderId(serviceProviderId)
      .serviceProviderName(serviceProviderName)
      .build();
  }
}
