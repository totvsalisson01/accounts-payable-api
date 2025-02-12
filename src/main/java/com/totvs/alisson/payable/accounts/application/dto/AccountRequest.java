package com.totvs.alisson.payable.accounts.application.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
public class AccountRequest {

  @NotNull(message = "Amount is required")
  @DecimalMin(value = "0.01", message = "Amount must be greater than or equal to 0.01")
  private BigDecimal amount;

  @NotBlank(message = "Description is required")
  @Size(max = 255, message = "Description must be less than 255 characters")
  private String description;

  @NotBlank(message = "Status is required")
  private String status;

  @NotNull(message = "Due date is required")
  @FutureOrPresent(message = "Due date must be in the present or future")
  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
  private LocalDate dueDate;

  @PastOrPresent(message = "Payment date must be in the past or present")
  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
  private LocalDate paymentDate;
}