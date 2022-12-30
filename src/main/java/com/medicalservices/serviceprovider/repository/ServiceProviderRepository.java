package com.medicalservices.serviceprovider.repository;

import com.medicalservices.serviceprovider.model.ServiceProvider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface ServiceProviderRepository extends JpaRepository<ServiceProvider, Long> {
  Optional<ServiceProvider> findByEmail(String email);
}
