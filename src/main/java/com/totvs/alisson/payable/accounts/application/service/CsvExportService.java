package com.totvs.alisson.payable.accounts.application.service;

import com.opencsv.CSVWriter;
import com.totvs.alisson.payable.accounts.application.dto.AccountCsvRecord;
import java.io.StringWriter;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class CsvExportService {

  public String generateCsvOutput(List<AccountCsvRecord> records) {
    StringWriter writer = new StringWriter();
    try (CSVWriter csvWriter = new CSVWriter(writer)) {
      String[] header = {
        "amount", "dueDate", "paymentDate", "description", "status", "importStatus", "errorMessage"
      };
      csvWriter.writeNext(header);

      for (AccountCsvRecord record : records) {
        String[] row = {
          record.getAmount().toString(),
          record.getDueDate().toString(),
          record.getPaymentDate() != null ? record.getPaymentDate().toString() : "",
          record.getDescription(),
          record.getStatus(),
          record.getImportStatus(),
          record.getErrorMessage() != null ? record.getErrorMessage() : ""
        };
        csvWriter.writeNext(row);
      }
    } catch (Exception e) {
      throw new RuntimeException("Failed to generate CSV output", e);
    }

    return writer.toString();
  }
}
