package com.totvs.alisson.payable.accounts.domain.enums;

import com.totvs.alisson.payable.accounts.domain.exception.InvalidAccountStatusException;

public enum AccountStatusEnum {
  PENDENTE,
  PAGO;

  public static AccountStatusEnum fromString(String value) {
    if (value == null) {
      throw new InvalidAccountStatusException(value);
    }

    for (AccountStatusEnum status : AccountStatusEnum.values()) {
      if (status.name().equalsIgnoreCase(value)) {
        return status;
      }
    }

    throw new InvalidAccountStatusException(value);
  }
}
