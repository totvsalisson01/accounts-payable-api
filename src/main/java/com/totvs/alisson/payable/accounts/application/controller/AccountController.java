package com.totvs.alisson.payable.accounts.application.controller;

import com.totvs.alisson.payable.accounts.application.dto.AccountStatusUpdateDTO;
import com.totvs.alisson.payable.accounts.domain.entity.Account;
import com.totvs.alisson.payable.accounts.domain.enums.AccountStatusEnum;
import com.totvs.alisson.payable.accounts.domain.exception.InvalidAccountStatusException;
import com.totvs.alisson.payable.accounts.domain.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/accounts")
@Tag(name = "Accounts Payable", description = "API for managing accounts payable")
public class AccountController {

  private final AccountService service;

  @Autowired
  public AccountController(AccountService service) {
    this.service = service;
  }

  @PostMapping
  @Operation(summary = "Create a new account", description = "Registers a new accounts payable")
  @ApiResponse(responseCode = "201", description = "Account successfully created")
  public ResponseEntity<Account> create(@Valid @RequestBody Account account) {
    Account savedAccount = service.create(account);
    return ResponseEntity.status(HttpStatus.CREATED).body(savedAccount);
  }

  @GetMapping
  @Operation(
      summary = "List all accounts",
      description = "Returns a paginated list of accounts payable")
  public ResponseEntity<Page<Account>> findAll(
      @RequestParam(required = false) String description,
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
          LocalDate dueDateStart,
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
          LocalDate dueDateEnd,
      Pageable pageable) {
    Page<Account> accounts = service.findAll(description, dueDateStart, dueDateEnd, pageable);
    return ResponseEntity.ok(accounts);
  }

  @GetMapping("/{id}")
  @Operation(
      summary = "Get an account by ID",
      description = "Returns details of a specific account")
  @ApiResponse(responseCode = "200", description = "Account found")
  @ApiResponse(responseCode = "404", description = "Account not found")
  public ResponseEntity<Account> getById(@PathVariable Long id) {
    Account account = service.getById(id);
    return ResponseEntity.ok(account);
  }

  @PutMapping("/{id}")
  @Operation(
      summary = "Update an account",
      description = "Updates the details of an existing account")
  @ApiResponse(responseCode = "200", description = "Account successfully updated")
  @ApiResponse(responseCode = "404", description = "Account not found")
  public ResponseEntity<Account> update(
      @PathVariable Long id, @Valid @RequestBody Account account) {
    Account updatedAccount = service.update(id, account);
    return ResponseEntity.ok(updatedAccount);
  }

  @PatchMapping("/{id}/status")
  @Operation(
      summary = "Update account status",
      description = "Changes the status of an existing account")
  @ApiResponse(responseCode = "200", description = "Status successfully updated")
  @ApiResponse(responseCode = "404", description = "Account not found")
  public ResponseEntity<Account> updateStatus(
      @PathVariable Long id, @Valid @RequestBody AccountStatusUpdateDTO accountStatusUpdateDTO) {
    Account updatedAccount =
        service.updateStatus(id, AccountStatusEnum.fromString(accountStatusUpdateDTO.getStatus()));
    return ResponseEntity.ok(updatedAccount);
  }

  @GetMapping("/total-paid")
  @Operation(
      summary = "Calculate total paid",
      description = "Returns the total amount paid within a period")
  public ResponseEntity<Double> getTotalPaid(
      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
    double totalPaid = service.getTotalPaid(startDate, endDate);
    return ResponseEntity.ok(totalPaid);
  }

  @DeleteMapping("/{id}")
  @Operation(summary = "Delete an account", description = "Removes an existing account")
  @ApiResponse(responseCode = "204", description = "Account successfully deleted")
  @ApiResponse(responseCode = "404", description = "Account not found")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    service.delete(id);
    return ResponseEntity.noContent().build();
  }

  @ExceptionHandler(InvalidAccountStatusException.class)
  public ResponseEntity<Map<String, Object>> handleBadRequest(Exception ex) {
    Map<String, Object> errorDetails = new LinkedHashMap<>();
    errorDetails.put("timestamp", LocalDateTime.now());
    errorDetails.put("message", ex.getMessage());
    errorDetails.put("status", 400);
    return ResponseEntity.badRequest().body(errorDetails);
  }
}
