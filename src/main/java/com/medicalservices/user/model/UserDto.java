package com.medicalservices.user.model;

import com.medicalservices.role.model.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import java.util.Date;
import java.util.List;

@Builder
@Data
@AllArgsConstructor
public class UserDto {
  private Long id;
  private String email;
  private String fullName;
  private Date birthDate;
  private Date lastCheckupDate;
  private List<Role> roles;
}
