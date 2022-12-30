package com.medicalservices.procedure.repository;

import com.medicalservices.procedure.model.Procedure;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProcedureRepository extends JpaRepository<Procedure, Long> {
  @Query("SELECT p FROM Procedure p ORDER BY p.id ASC")
  List<Procedure> findAllOrderById();
}
