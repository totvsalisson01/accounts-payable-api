package com.totvs.alisson.payable.accounts.application.service;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import com.totvs.alisson.payable.accounts.application.dto.AccountCsvRecord;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class CsvParserService {

  public List<AccountCsvRecord> parseCsvFile(MultipartFile file)
      throws IOException, CsvValidationException {
    List<AccountCsvRecord> records = new ArrayList<>();

    try (InputStreamReader reader = new InputStreamReader(file.getInputStream());
        CSVReader csvReader = new CSVReader(reader)) {

      String[] headers = csvReader.readNext();
      if (headers == null) {
        throw new IllegalArgumentException("CSV file is empty or missing headers");
      }

      Map<String, Integer> columnIndices = new HashMap<>();
      for (int i = 0; i < headers.length; i++) {
        columnIndices.put(headers[i].trim().toLowerCase(), i);
      }

      List<String> requiredColumns =
          Arrays.asList("amount", "duedate", "paymentdate", "description", "status");
      for (String column : requiredColumns) {
        if (!columnIndices.containsKey(column)) {
          throw new IllegalArgumentException("Missing required column: " + column);
        }
      }

      String[] nextRecord;
      while ((nextRecord = csvReader.readNext()) != null) {
        AccountCsvRecord record = new AccountCsvRecord();
        record.setAmount(new BigDecimal(nextRecord[columnIndices.get("amount")]));
        record.setDueDate(
            LocalDate.parse(
                nextRecord[columnIndices.get("duedate")], DateTimeFormatter.ISO_LOCAL_DATE));
        record.setPaymentDate(parseDate(nextRecord[columnIndices.get("paymentdate")]));
        record.setDescription(nextRecord[columnIndices.get("description")]);
        record.setStatus(nextRecord[columnIndices.get("status")]);

        records.add(record);
      }
    }

    return records;
  }

  private LocalDate parseDate(String dateString) {
    if (dateString == null || dateString.trim().isEmpty()) {
      return null;
    }
    return LocalDate.parse(dateString, DateTimeFormatter.ISO_LOCAL_DATE);
  }
}
