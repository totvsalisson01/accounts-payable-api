package com.totvs.alisson.payable.accounts.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthenticationResponse {

  private final String jwt;
}
