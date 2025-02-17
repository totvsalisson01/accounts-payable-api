package com.totvs.alisson.payable.accounts.domain.repository;

import com.totvs.alisson.payable.accounts.domain.entity.Account;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AccountRepositoryTest {

  @Mock private AccountRepository accountRepository;

  @Test
  public void testFindTotalPaidInPeriod() {
    LocalDate startDate = LocalDate.of(2023, 1, 1);
    LocalDate endDate = LocalDate.of(2023, 12, 31);
    Double expectedTotal = 1000.0;

    when(accountRepository.findTotalPaidInPeriod(startDate, endDate)).thenReturn(expectedTotal);

    Double actualTotal = accountRepository.findTotalPaidInPeriod(startDate, endDate);

    assertEquals(expectedTotal, actualTotal);
    verify(accountRepository, times(1)).findTotalPaidInPeriod(startDate, endDate);
  }

  @Test
  public void testFindAllFiltered_NoFilters() {
    Pageable pageable = Pageable.unpaged();
    Page<Account> expectedPage = new PageImpl<>(Collections.emptyList());

    when(accountRepository.findAllFiltered(null, null, null, pageable)).thenReturn(expectedPage);

    Page<Account> actualPage = accountRepository.findAllFiltered(null, null, null, pageable);

    assertEquals(expectedPage, actualPage);
    verify(accountRepository, times(1)).findAllFiltered(null, null, null, pageable);
  }

  @Test
  public void testFindAllFiltered_DescriptionFilter() {
    String description = "Test Description";
    Pageable pageable = Pageable.unpaged();
    Page<Account> expectedPage = new PageImpl<>(Collections.emptyList());

    when(accountRepository.findAllFiltered(description, null, null, pageable))
        .thenReturn(expectedPage);

    Page<Account> actualPage = accountRepository.findAllFiltered(description, null, null, pageable);

    assertEquals(expectedPage, actualPage);
    verify(accountRepository, times(1)).findAllFiltered(description, null, null, pageable);
  }

  @Test
  public void testFindAllFiltered_DueDateStartFilter() {
    LocalDate dueDateStart = LocalDate.of(2023, 1, 1);
    Pageable pageable = Pageable.unpaged();
    Page<Account> expectedPage = new PageImpl<>(Collections.emptyList());

    when(accountRepository.findAllFiltered(null, dueDateStart, null, pageable))
        .thenReturn(expectedPage);

    Page<Account> actualPage =
        accountRepository.findAllFiltered(null, dueDateStart, null, pageable);

    assertEquals(expectedPage, actualPage);
    verify(accountRepository, times(1)).findAllFiltered(null, dueDateStart, null, pageable);
  }

  @Test
  public void testFindAllFiltered_DueDateEndFilter() {
    LocalDate dueDateEnd = LocalDate.of(2023, 12, 31);
    Pageable pageable = Pageable.unpaged();
    Page<Account> expectedPage = new PageImpl<>(Collections.emptyList());

    when(accountRepository.findAllFiltered(null, null, dueDateEnd, pageable))
        .thenReturn(expectedPage);

    Page<Account> actualPage = accountRepository.findAllFiltered(null, null, dueDateEnd, pageable);

    assertEquals(expectedPage, actualPage);
    verify(accountRepository, times(1)).findAllFiltered(null, null, dueDateEnd, pageable);
  }

  @Test
  public void testFindAllFiltered_AllFilters() {
    String description = "Test Description";
    LocalDate dueDateStart = LocalDate.of(2023, 1, 1);
    LocalDate dueDateEnd = LocalDate.of(2023, 12, 31);
    Pageable pageable = Pageable.unpaged();
    Page<Account> expectedPage = new PageImpl<>(Collections.emptyList());

    when(accountRepository.findAllFiltered(description, dueDateStart, dueDateEnd, pageable))
        .thenReturn(expectedPage);

    Page<Account> actualPage =
        accountRepository.findAllFiltered(description, dueDateStart, dueDateEnd, pageable);

    assertEquals(expectedPage, actualPage);
    verify(accountRepository, times(1))
        .findAllFiltered(description, dueDateStart, dueDateEnd, pageable);
  }

  @Test
  public void testFindAllFiltered_DescriptionBlank() {
    String description = "   ";
    Pageable pageable = Pageable.unpaged();
    Page<Account> expectedPage = new PageImpl<>(Collections.emptyList());

    when(accountRepository.findAllFiltered(description, null, null, pageable))
        .thenReturn(expectedPage);

    Page<Account> actualPage = accountRepository.findAllFiltered(description, null, null, pageable);

    assertEquals(expectedPage, actualPage);
    verify(accountRepository, times(1)).findAllFiltered(description, null, null, pageable);
  }
}
