package com.medicalservices.procedure.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.medicalservices.serviceprovider.model.ServiceProvider;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "procedure")
@Getter
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class Procedure {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(nullable = false)
  private Long id;
  @Column(name = "procedure_name")
  private String procedureName;
  private String description;
  private float price;

  @ManyToOne
  @JoinColumn(name = "service_provider_id")
  @JsonIgnore
  @Setter
  private ServiceProvider serviceProvider;

  public Procedure(String procedureName, String description, float price, ServiceProvider serviceProvider) {
    this.procedureName = procedureName;
    this.description = description;
    this.price = price;
    this.serviceProvider = serviceProvider;
  }
}
