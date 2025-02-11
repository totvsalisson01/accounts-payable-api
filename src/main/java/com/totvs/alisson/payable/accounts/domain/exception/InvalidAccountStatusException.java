package com.totvs.alisson.payable.accounts.domain.exception;

import com.totvs.alisson.payable.accounts.domain.enums.AccountStatusEnum;
import java.util.Arrays;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidAccountStatusException extends RuntimeException {

  public InvalidAccountStatusException(String value) {
    super(
        String.format(
            "Invalid status value: '%s'. Allowed values: %s",
            value, Arrays.toString(AccountStatusEnum.values())));
  }
}
