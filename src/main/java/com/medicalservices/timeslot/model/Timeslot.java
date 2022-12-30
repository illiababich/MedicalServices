package com.medicalservices.timeslot.model;

import com.medicalservices.serviceprovider.model.ServiceProvider;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.sql.Timestamp;

@Entity
@Getter
@SuperBuilder(toBuilder = true)
@Table(name = "timeslots")
@NoArgsConstructor
public class Timeslot {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "start_time")
  private Timestamp startTime;

  @Column(name = "end_time")
  private Timestamp endTime;

  @ManyToOne
  @JoinColumn(name = "service_provider_id", referencedColumnName = "id")
  private ServiceProvider serviceProvider;

  public Timeslot(Timestamp startTime, Timestamp endTime, ServiceProvider serviceProvider) {
    this.startTime = startTime;
    this.endTime = endTime;
    this.serviceProvider = serviceProvider;
  }
}
