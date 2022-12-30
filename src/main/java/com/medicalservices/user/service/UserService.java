package com.medicalservices.user.service;

import com.medicalservices.role.model.UserRole;
import com.medicalservices.role.repository.RoleRepository;
import com.medicalservices.serviceprovider.model.ServiceProvider;
import com.medicalservices.serviceprovider.repository.ServiceProviderRepository;
import com.medicalservices.user.mapper.UserMapper;
import com.medicalservices.user.model.UserDto;
import com.medicalservices.user.repository.UserRepository;
import com.medicalservices.role.model.Role;
import com.medicalservices.user.model.User;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {
  private final UserRepository userRepository;
  private final ServiceProviderRepository serviceProviderRepository;
  private final BCryptPasswordEncoder bcryptpasswordencoder;
  private final RoleRepository roleRepository;

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    Optional<User> user = userRepository.findByEmail(email);
    Optional<ServiceProvider> optionalServiceProvider = serviceProviderRepository.findByEmail(email);

    Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();

    if (user.isPresent() && optionalServiceProvider.isEmpty()) {
      user.get().getRoles().forEach(role -> authorities.add(new SimpleGrantedAuthority(role.getRole().name())));
      return new org.springframework.security.core.userdetails.User(user.get().getEmail(),
        user.get().getPassword(), authorities);
    } else {
      authorities.add(new SimpleGrantedAuthority("SERVICE_PROVIDER"));
      return new org.springframework.security.core.userdetails.User(optionalServiceProvider.get().getEmail(),
        optionalServiceProvider.get().getPassword(), authorities);
    }
  }

  public void signUpUser(User user) {
    Optional<User> optionalUser = userRepository.findByEmail(user.getEmail());

    if (optionalUser.isPresent()) {
      throw new IllegalStateException("Email provided is already taken");
    }

    String encodedPassword = bcryptpasswordencoder.encode(user.getPassword());
    user.setPassword(encodedPassword);

    userRepository.save(user);
  }

  public void registerDefaultUser(User user) {
    Optional<Role> rolePatient = roleRepository.findByRole(UserRole.PATIENT);
    addRoleToUser(user.getId(), rolePatient.get().getRole());
  }

  public ResponseEntity<String> addRoleToUser(long userId, UserRole roleName) {
    User user = userRepository.findById(userId).orElseThrow(() -> new HttpServerErrorException(HttpStatus.NOT_FOUND));
    Role role = roleRepository.findByRole(roleName).orElseThrow(() ->
      new HttpServerErrorException(HttpStatus.NOT_FOUND));

    if (!user.getRoles().contains(role)) {
      user.getRoles().add(role);
      userRepository.save(user);
      return new ResponseEntity<>("The role was added successfully.", HttpStatus.OK);
    } else {
      return new ResponseEntity<>("The user already has that role.", HttpStatus.NOT_FOUND);
    }
  }

  public ResponseEntity<String> removeRoleFromUser(long userId, UserRole roleName) {
    User user = userRepository.findById(userId).orElseThrow(() -> new HttpServerErrorException(HttpStatus.NOT_FOUND));
    Role role = roleRepository.findByRole(roleName).orElseThrow(() ->
      new HttpServerErrorException(HttpStatus.NOT_FOUND));

    if (user.getRoles().contains(role)) {
      user.getRoles().remove(role);
      userRepository.save(user);
      return new ResponseEntity<>("The role was removed successfully.", HttpStatus.OK);
    } else {
      return new ResponseEntity<>("The user does not have that role.", HttpStatus.NOT_FOUND);
    }
  }

  public User getUserById(Long id) {
    return userRepository.findById(id).orElseThrow(() -> new HttpServerErrorException(HttpStatus.NOT_FOUND));
  }

  public Iterable<UserDto> getAllUsers() {
    return userRepository.findAll().stream().map(UserMapper::toDto)
      .collect(Collectors.toList());
  }

  public ResponseEntity<String> deleteUserById(Long userId) {
    try {
      userRepository.deleteById(userId);

      return new ResponseEntity<>("The user was deleted successfully.", HttpStatus.OK);
    } catch (NoSuchElementException exception) {
      return new ResponseEntity<>("The user with given ID not found.", HttpStatus.NOT_FOUND);
    }
  }

  public User patchUserById(Long userId, User userPatch) {
    User updatedUser = userRepository.findById(userId).map((user) -> user.toBuilder()
      .email(Optional.ofNullable(userPatch.getEmail()).orElse(user.getEmail()))
      .birthDate(Optional.ofNullable(userPatch.getBirthDate()).orElse(user.getBirthDate()))
      .firstName(Optional.ofNullable(userPatch.getFirstName()).orElse(user.getFirstName()))
      .lastName(Optional.ofNullable(userPatch.getLastName()).orElse(user.getLastName()))
      .enabled(Optional.ofNullable(userPatch.getEnabled()).orElse(user.getEnabled()))
      .lastCheckupDate(Optional.ofNullable(userPatch.getLastCheckupDate()).orElse(user.getLastCheckupDate()))
      .password(null != userPatch.getPassword()
          ? Optional.ofNullable(bcryptpasswordencoder.encode(userPatch.getPassword())).get()
          : user.getPassword())
      .build()
    ).orElseThrow(
      () -> new HttpClientErrorException(HttpStatus.NOT_FOUND, "The user with provided ID not found.")
    );

    userRepository.save(updatedUser);
    return updatedUser;
  }
}
