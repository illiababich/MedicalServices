package com.medicalservices.appointment.repository;

import com.medicalservices.appointment.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.Collection;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
  @Query("SELECT a FROM Appointment a WHERE a.user IS NULL ORDER BY a.timeslot.startTime")
  Collection<Appointment> findAvailableAppointments();

  @Query("SELECT a FROM Appointment a WHERE a.user IS NOT NULL ORDER BY a.timeslot.startTime")
  Collection<Appointment> findUnavailableAppointments();

  @Query("SELECT a FROM Appointment a WHERE a.user.email = ?1")
  Collection<Appointment> findUserAppointments(String email);
}
