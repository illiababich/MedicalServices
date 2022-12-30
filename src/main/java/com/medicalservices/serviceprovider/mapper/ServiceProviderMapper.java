package com.medicalservices.serviceprovider.mapper;

import com.medicalservices.serviceprovider.model.ServiceProvider;
import com.medicalservices.serviceprovider.model.ServiceProviderDto;
import com.medicalservices.procedure.mapper.ProcedureMapper;
import java.util.stream.Collectors;

public class ServiceProviderMapper {

  public static ServiceProviderDto toDto(ServiceProvider serviceProvider) {
    return ServiceProviderDto.builder()
      .id(serviceProvider.getId())
      .name(serviceProvider.getCompanyName())
      .procedures(serviceProvider.getProcedures().stream()
        .map(ProcedureMapper::toDto)
        .collect(Collectors.toList()))
      .build();
  }

}
