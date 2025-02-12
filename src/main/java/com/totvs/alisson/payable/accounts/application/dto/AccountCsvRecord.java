package com.totvs.alisson.payable.accounts.application.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Data;

@Data
public class AccountCsvRecord {
  private BigDecimal amount;
  private LocalDate dueDate;
  private LocalDate paymentDate;
  private String description;
  private String status;
  private String importStatus;
  private String errorMessage;
}
