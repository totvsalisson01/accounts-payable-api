package com.totvs.alisson.payable.accounts.application.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class AccountStatusUpdateDTO {

  @NotNull private String status;
}
