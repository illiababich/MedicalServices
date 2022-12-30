package com.medicalservices.role.controller;

import com.medicalservices.role.model.Role;
import com.medicalservices.role.service.RoleService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/roles")
@PreAuthorize("hasAuthority('SCOPE_ADMIN')")
public class RoleController {
  private final RoleService roleService;

  @GetMapping()
  @ResponseStatus(HttpStatus.OK)
  public Iterable<Role> getAllRoles() {
    return roleService.getAllRoles();
  }

  @PostMapping(path = "/add/{userId}/{role}")
  public ResponseEntity<String> addRoleToUserById(@PathVariable("userId") Long userId,
    @PathVariable("role")String role) {
    return roleService.addRoleToUser(userId, role);
  }

  @PostMapping(path = "/remove/{userId}/{role}")
  public ResponseEntity<String> removeRoleFromUser(@PathVariable("userId") Long userId,
    @PathVariable("role") String role) {
    return roleService.removeRoleFromUser(userId, role);
  }
}
