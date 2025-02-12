package com.totvs.alisson.payable.accounts.application.dto;

import com.totvs.alisson.payable.accounts.domain.entity.Account;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Getter;

@Getter
public class AccountResponse {
  private Long id;
  private BigDecimal amount;
  private String description;
  private String status;
  private LocalDate dueDate;
  private LocalDate paymentDate;

  public AccountResponse(Account account) {
    this.id = account.getId();
    this.amount = account.getAmount();
    this.description = account.getDescription();
    this.status = account.getStatus();
    this.dueDate = account.getDueDate();
    this.paymentDate = account.getPaymentDate();
  }
}
