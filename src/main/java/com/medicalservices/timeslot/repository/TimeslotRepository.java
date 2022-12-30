package com.medicalservices.timeslot.repository;

import com.medicalservices.timeslot.model.Timeslot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.Collection;

@Repository
public interface TimeslotRepository extends JpaRepository<Timeslot, Long> {

  @Query("SELECT t from Timeslot t where (SELECT count(a) from Appointment a "
    + "where a.timeslot.id = t.id and a.user IS NOT NULL) = 0 "
    + "and t.startTime > CURRENT_TIMESTAMP")
  Collection<Timeslot> findAvailableTimeslots();

}
