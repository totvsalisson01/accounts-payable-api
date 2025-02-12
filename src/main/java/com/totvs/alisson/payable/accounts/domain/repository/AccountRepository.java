package com.totvs.alisson.payable.accounts.domain.repository;

import com.totvs.alisson.payable.accounts.domain.entity.Account;
import java.time.LocalDate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

  @Query(
      "SELECT SUM(COALESCE(c.amount, 0)) FROM Account c WHERE c.paymentDate BETWEEN :startDate AND :endDate")
  Double findTotalPaidInPeriod(
      @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
}
