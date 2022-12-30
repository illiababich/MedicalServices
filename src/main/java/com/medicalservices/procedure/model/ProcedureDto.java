package com.medicalservices.procedure.model;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ProcedureDto {

  private Long id;
  private String name;
  private String description;
  private float price;

}
