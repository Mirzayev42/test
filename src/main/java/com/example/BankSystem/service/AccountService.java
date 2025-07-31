package com.example.BankSystem.service;

import com.example.BankSystem.exception.AccountNotFoundException;
import com.example.BankSystem.model.Account;
import com.example.BankSystem.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;


    public Account createAccount(Account account) {
        return accountRepository.save(account);
    }

    public Account getAccountById(Long id) throws AccountNotFoundException {
        return accountRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException("Account not found with id " + id));
    }

    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    public void transfer(Long fromAccountId, Long toAccountId, long amountInCents) {
        Account from = accountRepository.findById(fromAccountId)
                .orElseThrow(() -> new RuntimeException("Sender account not found"));

        Account to = accountRepository.findById(toAccountId)
                .orElseThrow(() -> new RuntimeException("Receiver account not found"));

        if (from.getBalanceInCents() < amountInCents) {
            throw new RuntimeException("Insufficient balance in sender account");
        }
        from.setBalanceInCents(from.getBalanceInCents() - amountInCents);
        to.setBalanceInCents(to.getBalanceInCents() + amountInCents);

        accountRepository.save(from);
        accountRepository.save(to);
    }

}

