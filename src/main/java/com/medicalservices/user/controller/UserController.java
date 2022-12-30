package com.medicalservices.user.controller;

import com.medicalservices.user.model.UserDto;
import com.medicalservices.user.service.UserService;
import com.medicalservices.user.model.User;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/users")
public class UserController {
  private final UserService userService;

  @PreAuthorize("hasAnyAuthority('SCOPE_ADMIN')")
  @GetMapping("/{userId}")
  public User getUserById(@PathVariable("userId") Long id) {
    return userService.getUserById(id);
  }

  @PreAuthorize("hasAnyAuthority('SCOPE_ADMIN', 'SCOPE_MANAGER')")
  @GetMapping()
  @ResponseStatus(HttpStatus.OK)
  public Iterable<UserDto> getAllUsers() {
    return userService.getAllUsers();
  }

  @PreAuthorize("hasAnyAuthority('SCOPE_ADMIN')")
  @DeleteMapping("/{userId}")
  public ResponseEntity<String> deleteUser(@PathVariable("userId") Long userId) {
    return userService.deleteUserById(userId);
  }

  @PreAuthorize("hasAnyAuthority('SCOPE_ADMIN', 'SCOPE_MANAGER')")
  @PatchMapping(path = "/{userId}", consumes = "application/json")
  public User patchUser(@PathVariable("userId") Long userId, @RequestBody User patch) {
    return userService.patchUserById(userId, patch);
  }
}
