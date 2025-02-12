package com.totvs.alisson.payable.accounts.domain.validation;

import com.totvs.alisson.payable.accounts.application.dto.AccountRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AccountValidator {

  private final Validator validator;

  @Autowired
  public AccountValidator(Validator validator) {
    this.validator = validator;
  }

  public List<String> validate(AccountRequest request) {
    Set<ConstraintViolation<AccountRequest>> violations = validator.validate(request);
    List<String> errorMessages = new ArrayList<>();
    for (ConstraintViolation<AccountRequest> violation : violations) {
      errorMessages.add(violation.getMessage());
    }
    return errorMessages;
  }
}
