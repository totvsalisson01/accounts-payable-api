package com.totvs.alisson.payable.accounts.domain.service;

import com.totvs.alisson.payable.accounts.application.dto.AccountCsvRecord;
import com.totvs.alisson.payable.accounts.application.dto.AccountRequest;
import com.totvs.alisson.payable.accounts.domain.entity.Account;
import com.totvs.alisson.payable.accounts.domain.enums.AccountStatusEnum;
import com.totvs.alisson.payable.accounts.domain.exception.AccountNotFoundException;
import com.totvs.alisson.payable.accounts.domain.factory.AccountFactory;
import com.totvs.alisson.payable.accounts.domain.repository.AccountRepository;
import com.totvs.alisson.payable.accounts.domain.validation.AccountValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {

  @Mock private AccountRepository repository;

  @Mock private AccountValidator validator;

  @InjectMocks private AccountService accountService;

  @Test
  public void testCreateAccount() {
    AccountRequest request = new AccountRequest();
    request.setAmount(BigDecimal.valueOf(100.0));
    request.setDueDate(LocalDate.now());
    request.setDescription("Test Description");
    request.setStatus("PAGO");

    Account account = AccountFactory.createFrom(request);
    when(repository.save(any(Account.class))).thenReturn(account);

    Account createdAccount = accountService.create(request);

    assertNotNull(createdAccount);
    assertEquals(request.getAmount(), createdAccount.getAmount());
    verify(repository, times(1)).save(any(Account.class));
  }

  @Test
  public void testUpdateAccount() {
    Long id = 1L;
    AccountRequest request = new AccountRequest();
    request.setAmount(BigDecimal.valueOf(200.0));
    request.setDueDate(LocalDate.now());
    request.setDescription("Updated Description");
    request.setStatus("PENDENTE");

    Account existingAccount = new Account();
    existingAccount.setId(id);

    when(repository.findById(id)).thenReturn(Optional.of(existingAccount));
    when(repository.save(any(Account.class))).thenReturn(existingAccount);

    Account updatedAccount = accountService.update(id, request);

    assertNotNull(updatedAccount);
    assertEquals(id, updatedAccount.getId());
    verify(repository, times(1)).save(any(Account.class));
  }

  @Test
  public void testGetAccountById() {
    Long id = 1L;
    Account account = new Account();
    account.setId(id);

    when(repository.findById(id)).thenReturn(Optional.of(account));

    Account foundAccount = accountService.getById(id);

    assertNotNull(foundAccount);
    assertEquals(id, foundAccount.getId());
    verify(repository, times(1)).findById(id);
  }

  @Test
  public void testGetAccountByIdNotFound() {
    Long id = 1L;

    when(repository.findById(id)).thenReturn(Optional.empty());

    assertThrows(AccountNotFoundException.class, () -> accountService.getById(id));
    verify(repository, times(1)).findById(id);
  }

  @Test
  public void testUpdateAccountStatus() {
    Long id = 1L;
    AccountStatusEnum status = AccountStatusEnum.PAGO;
    Account account = new Account();
    account.setId(id);

    when(repository.findById(id)).thenReturn(Optional.of(account));
    when(repository.save(any(Account.class))).thenReturn(account);

    Account updatedAccount = accountService.updateStatus(id, status);

    assertNotNull(updatedAccount);
    assertEquals(status.name(), updatedAccount.getStatus());
    verify(repository, times(1)).save(any(Account.class));
  }

  @Test
  public void testGetTotalPaid() {
    LocalDate startDate = LocalDate.of(2023, 1, 1);
    LocalDate endDate = LocalDate.of(2023, 12, 31);
    Double expectedTotal = 1000.0;

    when(repository.findTotalPaidInPeriod(startDate, endDate)).thenReturn(expectedTotal);

    Double actualTotal = accountService.getTotalPaid(startDate, endDate);

    assertEquals(expectedTotal, actualTotal);
    verify(repository, times(1)).findTotalPaidInPeriod(startDate, endDate);
  }

  @Test
  public void testDeleteAccount() {
    Long id = 1L;
    Account account = new Account();
    account.setId(id);

    when(repository.findById(id)).thenReturn(Optional.of(account));
    doNothing().when(repository).deleteById(id);

    accountService.delete(id);

    verify(repository, times(1)).deleteById(id);
  }

  @Test
  public void testSaveAllFromCsv() {
    AccountCsvRecord record = new AccountCsvRecord();
    record.setAmount(BigDecimal.valueOf(100.0));
    record.setDueDate(LocalDate.now());
    record.setDescription("Test Description");
    record.setStatus("PENDENTE");

    List<AccountCsvRecord> records = Collections.singletonList(record);

    when(validator.validate(any(AccountRequest.class))).thenReturn(Collections.emptyList());
    when(repository.save(any(Account.class))).thenReturn(new Account());

    List<AccountCsvRecord> processedRecords = accountService.saveAllFromCsv(records);

    assertNotNull(processedRecords);
    assertEquals(1, processedRecords.size());
    assertEquals("SUCCESS", processedRecords.get(0).getImportStatus());
    verify(repository, times(1)).save(any(Account.class));
  }
}
