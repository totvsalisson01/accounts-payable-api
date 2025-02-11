package com.totvs.alisson.payable.accounts.domain.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class AccountNotFoundException extends RuntimeException {
  public AccountNotFoundException(Long id) {
    super(String.format("Account with id %d not found", id));
  }
}
