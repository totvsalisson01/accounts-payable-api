package com.totvs.alisson.payable.accounts.domain.repository;

import com.totvs.alisson.payable.accounts.domain.entity.Account;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository
    extends JpaRepository<Account, Long>, JpaSpecificationExecutor<Account> {

  @Query(
      "SELECT SUM(COALESCE(c.amount, 0)) FROM Account c WHERE c.paymentDate BETWEEN :startDate AND :endDate")
  Double findTotalPaidInPeriod(
      @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

  default Page<Account> findAllFiltered(
      String description, LocalDate dueDateStart, LocalDate dueDateEnd, Pageable pageable) {

    Specification<Account> spec =
        (root, query, criteriaBuilder) -> {
          List<Predicate> predicates = new ArrayList<>();

          if (description != null && !description.isBlank()) {
            predicates.add(
                criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("description")),
                    "%" + description.toLowerCase() + "%"));
          }

          if (dueDateStart != null) {
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("dueDate"), dueDateStart));
          }

          if (dueDateEnd != null) {
            predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("dueDate"), dueDateEnd));
          }

          return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

    return findAll(spec, pageable);
  }
}
