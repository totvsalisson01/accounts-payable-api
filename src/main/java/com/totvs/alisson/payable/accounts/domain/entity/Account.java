package com.totvs.alisson.payable.accounts.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Data;

@Data
@Entity
@Table(name = "contas")
public class Account {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @JsonIgnore
  private Long id;

  @Column(name = "valor", precision = 12, scale = 2, nullable = false)
  private BigDecimal amount;

  @Column(name = "data_vencimento", nullable = false)
  private LocalDate dueDate;

  @Column(name = "data_pagamento")
  private LocalDate paymentDate;

  @Column(name = "descricao", nullable = false)
  private String description;

  @Column(name = "situacao")
  private String status;
}
