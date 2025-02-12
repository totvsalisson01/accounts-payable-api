package com.totvs.alisson.payable.accounts.application.controller;

import com.totvs.alisson.payable.accounts.application.dto.AuthenticationRequest;
import com.totvs.alisson.payable.accounts.application.dto.AuthenticationResponse;
import com.totvs.alisson.payable.accounts.application.dto.RegistrationRequest;
import com.totvs.alisson.payable.accounts.application.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

  @Autowired private AuthService authService;

  @PostMapping("/authenticate")
  public ResponseEntity<?> createAuthenticationToken(
      @RequestBody AuthenticationRequest authenticationRequest) throws Exception {
    try {
      AuthenticationResponse response = authService.authenticate(authenticationRequest);
      return ResponseEntity.ok(response);
    } catch (AuthenticationException e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
    }
  }

  @PostMapping("/register")
  public ResponseEntity<?> registerUser(@RequestBody RegistrationRequest registrationRequest) {
    try {
      authService.register(registrationRequest);
      return ResponseEntity.ok("User registered successfully");
    } catch (IllegalArgumentException e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }
}
