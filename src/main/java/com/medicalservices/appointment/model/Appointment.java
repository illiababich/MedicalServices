package com.medicalservices.appointment.model;

import com.medicalservices.procedure.model.Procedure;
import com.medicalservices.timeslot.model.Timeslot;
import com.medicalservices.user.model.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Entity
@SuperBuilder(toBuilder = true)
@Table(name = "appointment")
@Getter
@RequiredArgsConstructor
public class Appointment {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @OneToOne
  @JoinColumn(name = "timeslot_id")
  private Timeslot timeslot;

  @ManyToOne
  @JoinColumn(name = "user_id")
  private User user;

  private String comment;

  @Setter
  @ManyToMany
  @JoinTable(name = "appointment_procedures", joinColumns = @JoinColumn(name = "appointment_id"),
    inverseJoinColumns = @JoinColumn(name = "procedure_id"))
  private List<Procedure> procedures = new ArrayList<>();

  public Appointment(User user, Timeslot timeslot, String comment) {
    this.user = user;
    this.timeslot = timeslot;
    this.comment = comment;
  }

  public Appointment(Timeslot timeslot, String comment) {
    this.timeslot = timeslot;
    this.comment = comment;
  }
}
