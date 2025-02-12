package com.totvs.alisson.payable.accounts.application.service;

import com.totvs.alisson.payable.accounts.application.dto.AuthenticationRequest;
import com.totvs.alisson.payable.accounts.application.dto.AuthenticationResponse; // Make sure you
                                                                                  // have this DTO
import com.totvs.alisson.payable.accounts.application.dto.RegistrationRequest;
import com.totvs.alisson.payable.accounts.domain.entity.User;
import com.totvs.alisson.payable.accounts.domain.repository.UserRepository;
import com.totvs.alisson.payable.accounts.security.JwtUtil;
import com.totvs.alisson.payable.accounts.security.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired; // Import and use @Autowired
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

  @Autowired
  private AuthenticationManager authenticationManager;

  @Autowired
  private JwtUtil jwtUtil;

  @Autowired
  private UserDetailsServiceImpl userDetailsService;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private BCryptPasswordEncoder passwordEncoder;

  public AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest)
      throws Exception {
    try {
      authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(
              authenticationRequest.getUsername(), authenticationRequest.getPassword()));
    } catch (AuthenticationException e) {
      throw new Exception("Incorrect username or password", e);
    }

    final UserDetails userDetails =
        userDetailsService.loadUserByUsername(authenticationRequest.getUsername());

    final String jwt = jwtUtil.generateToken(userDetails);

    return new AuthenticationResponse(jwt);
  }

  public void register(RegistrationRequest registrationRequest) {
    if (userRepository.existsByUsername(registrationRequest.getUsername())) {
      throw new IllegalArgumentException("Username is already taken");
    }

    User user = new User();
    user.setUsername(registrationRequest.getUsername());
    user.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
    userRepository.save(user);
  }
}
