package com.totvs.alisson.payable.accounts.application.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Getter;

@Getter
public class AccountRequest {
  private BigDecimal amount;
  private String description;
  private String status;
  private LocalDate dueDate;
  private LocalDate paymentDate;
}
