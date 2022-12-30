package com.medicalservices.procedure.mapper;

import com.medicalservices.procedure.model.Procedure;
import com.medicalservices.procedure.model.ProcedureDto;

public class ProcedureMapper {

  public static ProcedureDto toDto(Procedure procedure) {
    return ProcedureDto.builder()
      .id(procedure.getId())
      .name(procedure.getProcedureName())
      .description(procedure.getDescription())
      .price(procedure.getPrice())
      .build();
  }

}
