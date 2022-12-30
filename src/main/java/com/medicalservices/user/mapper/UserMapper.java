package com.medicalservices.user.mapper;

import com.medicalservices.user.model.UserDto;
import com.medicalservices.user.model.User;

public class UserMapper {

  public static UserDto toDto(User user) {
    return UserDto.builder()
      .id(user.getId())
      .email(user.getEmail())
      .fullName(user.getFullName())
      .birthDate(user.getBirthDate())
      .lastCheckupDate(user.getLastCheckupDate())
      .roles(user.getRoles())
      .build();
  }

}
