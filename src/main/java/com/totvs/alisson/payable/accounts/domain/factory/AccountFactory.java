package com.totvs.alisson.payable.accounts.domain.factory;

import com.totvs.alisson.payable.accounts.application.dto.AccountRequest;
import com.totvs.alisson.payable.accounts.domain.entity.Account;
import com.totvs.alisson.payable.accounts.domain.enums.AccountStatusEnum;

public class AccountFactory {

  public static Account createFrom(AccountRequest accountRequest) {
    Account account = new Account();
    account.setAmount(accountRequest.getAmount());
    account.setDescription(accountRequest.getDescription());

    account.setDueDate(accountRequest.getDueDate());
    account.setPaymentDate(accountRequest.getPaymentDate());

    AccountStatusEnum statusEnum = AccountStatusEnum.fromString(accountRequest.getStatus());
    account.setStatus(statusEnum.name());

    return account;
  }
}
