package com.totvs.alisson.payable.accounts.application.controller;

import com.totvs.alisson.payable.accounts.application.dto.*;
import com.totvs.alisson.payable.accounts.application.service.CsvExportService;
import com.totvs.alisson.payable.accounts.application.service.CsvParserService;
import com.totvs.alisson.payable.accounts.domain.entity.Account;
import com.totvs.alisson.payable.accounts.domain.enums.AccountStatusEnum;
import com.totvs.alisson.payable.accounts.domain.exception.InvalidAccountStatusException;
import com.totvs.alisson.payable.accounts.domain.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/accounts")
@Tag(name = "Accounts Payable", description = "API for managing accounts payable")
@SecurityRequirement(name = "bearerAuth")
public class AccountController {

  private final AccountService accountService;
  private final CsvParserService csvParserService;
  private final CsvExportService csvExportService;

  @Autowired
  public AccountController(
      AccountService accountService,
      CsvParserService csvParserService,
      CsvExportService csvExportService) {
    this.accountService = accountService;
    this.csvParserService = csvParserService;
    this.csvExportService = csvExportService;
  }

  @PostMapping
  @Operation(summary = "Create a new account", description = "Registers a new account payable")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "201",
            description = "Account successfully created",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = AccountResponse.class))),
        @ApiResponse(
            responseCode = "400",
            description = "Bad Request",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class)))
      })
  public ResponseEntity<AccountResponse> create(@Valid @RequestBody AccountRequest request) {
    Account account = accountService.create(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(new AccountResponse(account));
  }

  @GetMapping
  @Operation(
      summary = "List all accounts",
      description = "Returns a paginated list of accounts payable")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Accounts found",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = Page.class))),
        @ApiResponse(
            responseCode = "400",
            description = "Bad Request",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class)))
      })
  public ResponseEntity<Page<AccountResponse>> findAll(
      @RequestParam(required = false) String description,
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
          LocalDate dueDateStart,
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
          LocalDate dueDateEnd,
      Pageable pageable) {
    Page<Account> accountsPage =
        accountService.findAll(description, dueDateStart, dueDateEnd, pageable);
    Page<AccountResponse> accountResponsesPage = accountsPage.map(AccountResponse::new);

    return ResponseEntity.ok(accountResponsesPage);
  }

  @GetMapping("/{id}")
  @Operation(
      summary = "Get an account by ID",
      description = "Returns details of a specific account")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Account found",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = AccountResponse.class))),
        @ApiResponse(
            responseCode = "404",
            description = "Account not found",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class)))
      })
  public ResponseEntity<AccountResponse> getById(@PathVariable Long id) {
    Account account = accountService.getById(id);
    AccountResponse accountResponse = new AccountResponse(account);
    return ResponseEntity.ok(accountResponse);
  }

  @PutMapping("/{id}")
  @Operation(summary = "Update an account", description = "Updates an existing account")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Account successfully updated",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = AccountResponse.class))),
        @ApiResponse(
            responseCode = "400",
            description = "Bad Request",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(
            responseCode = "404",
            description = "Account not found",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class)))
      })
  public ResponseEntity<AccountResponse> update(
      @PathVariable Long id, @Valid @RequestBody AccountRequest request) {
    Account updatedAccount = accountService.update(id, request);
    return ResponseEntity.ok(new AccountResponse(updatedAccount));
  }

  @PatchMapping("/{id}/status")
  @Operation(
      summary = "Update account status",
      description = "Changes the status of an existing account")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Status successfully updated",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = AccountResponse.class))),
        @ApiResponse(
            responseCode = "400",
            description = "Bad Request",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(
            responseCode = "404",
            description = "Account not found",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class)))
      })
  public ResponseEntity<AccountResponse> updateStatus(
      @PathVariable Long id, @Valid @RequestBody AccountStatusUpdateDTO accountStatusUpdateDTO) {

    Account updatedAccount =
        accountService.updateStatus(
            id, AccountStatusEnum.fromString(accountStatusUpdateDTO.getStatus()));

    AccountResponse accountResponse = new AccountResponse(updatedAccount);
    return ResponseEntity.ok(accountResponse);
  }

  @GetMapping("/total-paid")
  @Operation(
      summary = "Calculate total paid",
      description = "Returns the total amount paid within a period")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Total paid calculated",
            content = @Content(mediaType = "application/json", schema = @Schema(type = "number"))),
        @ApiResponse(
            responseCode = "400",
            description = "Bad Request",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class)))
      })
  public ResponseEntity<Double> getTotalPaid(
      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
    double totalPaid = accountService.getTotalPaid(startDate, endDate);
    return ResponseEntity.ok(totalPaid);
  }

  @DeleteMapping("/{id}")
  @Operation(summary = "Delete an account", description = "Removes an existing account")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "204", description = "Account successfully deleted"),
        @ApiResponse(
            responseCode = "404",
            description = "Account not found",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class)))
      })
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    accountService.delete(id);
    return ResponseEntity.noContent().build();
  }

  @PostMapping(value = "/import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  @Operation(
      summary = "Import accounts from a CSV file",
      description = "Upload a CSV file to import accounts.")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "CSV file processed successfully. Returns a CSV file with results.",
            content = @Content(mediaType = "text/plain", schema = @Schema(type = "string"))),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid file or file format.",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error.",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class)))
      })
  public ResponseEntity<?> importAccountsFromCsv(
      @RequestPart("file")
          @Parameter(
              description = "CSV file to be uploaded",
              content =
                  @Content(
                      mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
                      schema = @Schema(type = "string", format = "binary")))
          MultipartFile file) {
    try {
      List<AccountCsvRecord> records = csvParserService.parseCsvFile(file);
      List<AccountCsvRecord> processedRecords = accountService.saveAllFromCsv(records);
      String outputCsv = csvExportService.generateCsvOutput(processedRecords);

      return ResponseEntity.ok()
          .header("Content-Disposition", "attachment; filename=import_results.csv")
          .contentType(MediaType.TEXT_PLAIN)
          .body(outputCsv);
    } catch (IOException e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body("Failed to read the CSV file: " + e.getMessage());
    } catch (IllegalArgumentException e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body("Failed to process the CSV file: " + e.getMessage());
    }
  }

  @ExceptionHandler(InvalidAccountStatusException.class)
  public ResponseEntity<ErrorResponse> handleBadRequest(InvalidAccountStatusException ex) {
    ErrorResponse errorResponse =
        new ErrorResponse(LocalDateTime.now(), HttpStatus.BAD_REQUEST.value(), ex.getMessage());
    return ResponseEntity.badRequest().body(errorResponse);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleValidationExceptions(
      MethodArgumentNotValidException ex) {

    FieldError error = ex.getBindingResult().getFieldError();

    String errorMessage;
    if (error != null) {
      errorMessage = error.getDefaultMessage();
    } else {
      errorMessage = "Validation error";
    }

    ErrorResponse errorResponse =
        new ErrorResponse(LocalDateTime.now(), HttpStatus.BAD_REQUEST.value(), errorMessage);

    return ResponseEntity.badRequest().body(errorResponse);
  }
}
