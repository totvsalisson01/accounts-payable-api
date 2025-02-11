package com.totvs.alisson.payable.accounts.domain.service;

import com.totvs.alisson.payable.accounts.domain.entity.Account;
import com.totvs.alisson.payable.accounts.domain.enums.AccountStatusEnum;
import com.totvs.alisson.payable.accounts.domain.exception.AccountNotFoundException;
import com.totvs.alisson.payable.accounts.domain.repository.AccountRepository;
import java.time.LocalDate;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class AccountService {

  private final AccountRepository repository;

  @Autowired
  public AccountService(AccountRepository repository) {
    this.repository = repository;
  }

  public Account create(Account account) {
    return repository.save(account);
  }

  public Page<Account> findAll(
      String description, LocalDate dueDateStart, LocalDate dueDateEnd, Pageable pageable) {
    return repository.findAll(pageable);
  }

  public Account getById(Long id) {
    return repository.findById(id).orElseThrow(() -> new AccountNotFoundException(id));
  }

  public Account update(Long id, Account account) {
    getById(id);
    account.setId(id);
    return repository.save(account);
  }

  public Account updateStatus(Long id, AccountStatusEnum status) {
    Account account = getById(id);
    account.setStatus(status.name());
    return repository.save(account);
  }

  public double getTotalPaid(LocalDate startDate, LocalDate endDate) {
    return Optional.ofNullable(repository.findTotalPaidInPeriod(startDate, endDate)).orElse(0.0);
  }

  public void delete(Long id) {
    getById(id);
    repository.deleteById(id);
  }
}
