package com.medicalservices.serviceprovider.model;

import com.medicalservices.procedure.model.Procedure;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Entity
@Data
@SuperBuilder(toBuilder = true)
@Table(name = "service_provider")
@NoArgsConstructor
public class ServiceProvider implements UserDetails {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String email;
  @Getter
  private String password;
  @Column(name = "company_name")
  private String companyName;
  @Column(name = "phone_number")
  private String phoneNumber;
  private Boolean enabled;
  @OneToMany(mappedBy = "serviceProvider")
  private List<Procedure> procedures;

  public ServiceProvider(String email, String password, String companyName, String phoneNumber) {
    this.email = email;
    this.password = password;
    this.companyName = companyName;
    this.phoneNumber = phoneNumber;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return Collections.singleton(new SimpleGrantedAuthority("SERVICE_PROVIDER"));
  }

  @Override
  public String getUsername() {
    return email;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return enabled;
  }
}
