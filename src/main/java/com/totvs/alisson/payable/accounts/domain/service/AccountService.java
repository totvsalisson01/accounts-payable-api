package com.totvs.alisson.payable.accounts.domain.service;

import com.totvs.alisson.payable.accounts.application.dto.AccountCsvRecord;
import com.totvs.alisson.payable.accounts.application.dto.AccountRequest;
import com.totvs.alisson.payable.accounts.domain.entity.Account;
import com.totvs.alisson.payable.accounts.domain.enums.AccountStatusEnum;
import com.totvs.alisson.payable.accounts.domain.exception.AccountNotFoundException;
import com.totvs.alisson.payable.accounts.domain.factory.AccountFactory;
import com.totvs.alisson.payable.accounts.domain.repository.AccountRepository;
import com.totvs.alisson.payable.accounts.domain.validation.AccountValidator;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class AccountService {

  private final AccountRepository repository;
  private final AccountValidator validator;

  @Autowired
  public AccountService(AccountRepository repository, AccountValidator validator) {
    this.repository = repository;
    this.validator = validator;
  }

  public Account create(AccountRequest request) {
    Account account = AccountFactory.createFrom(request);
    return repository.save(account);
  }

  public Account update(Long id, AccountRequest request) {
    getById(id);
    Account updatedAccount = AccountFactory.createFrom(request);
    updatedAccount.setId(id);
    return repository.save(updatedAccount);
  }

  public Page<Account> findAll(
      String description, LocalDate dueDateStart, LocalDate dueDateEnd, Pageable pageable) {
    return repository.findAllFiltered(description, dueDateStart, dueDateEnd, pageable);
  }

  public Account getById(Long id) {
    return repository.findById(id).orElseThrow(() -> new AccountNotFoundException(id));
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

  public List<AccountCsvRecord> saveAllFromCsv(List<AccountCsvRecord> records) {
    List<AccountCsvRecord> processedRecords = new ArrayList<>();

    for (AccountCsvRecord record : records) {
      try {
        AccountRequest accountRequest = createAccountRequestFromRecord(record);
        List<String> validationErrors = validator.validate(accountRequest);

        if (!validationErrors.isEmpty()) {
          record.setImportStatus("ERROR");
          record.setErrorMessage(String.join(". ", validationErrors));
        } else {
          Account account = AccountFactory.createFrom(accountRequest);
          repository.save(account);
          record.setImportStatus("SUCCESS");
          record.setErrorMessage(null);
        }

      } catch (Exception e) {
        record.setImportStatus("ERROR");
        record.setErrorMessage("An unexpected error occurred: " + e.getMessage());
      }

      processedRecords.add(record);
    }

    return processedRecords;
  }

  private AccountRequest createAccountRequestFromRecord(AccountCsvRecord record) {
    AccountRequest accountRequest = new AccountRequest();
    accountRequest.setAmount(record.getAmount());
    accountRequest.setDueDate(record.getDueDate());
    accountRequest.setPaymentDate(record.getPaymentDate());
    accountRequest.setDescription(record.getDescription());
    accountRequest.setStatus(record.getStatus());
    return accountRequest;
  }
}
