package com.example.BankSystem.service;

import com.example.BankSystem.exception.AccountNotFoundException;
import com.example.BankSystem.exception.InsufficientFundsException;
import com.example.BankSystem.model.Account;
import com.example.BankSystem.repository.AccountRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class TransferService {

    private final AccountRepository accountRepository;


    @Transactional
    public void transfer(Long fromAccountId, Long toAccountId, long amountInCents) {
        log.info("🟢 Transfer started: fromAccountId={}, toAccountId={}, amountInCents={}", fromAccountId, toAccountId, amountInCents);

        Account from = accountRepository.findById(fromAccountId)
                .orElseThrow(() -> {
                    log.error("❌ Sender account not found: {}", fromAccountId);
                    return new AccountNotFoundException("Sender account not found");
                });

        Account to = accountRepository.findById(toAccountId)
                .orElseThrow(() -> {
                    log.error("❌ Receiver account not found: {}", toAccountId);
                    return new AccountNotFoundException("Receiver account not found");
                });

        if (from.getBalanceInCents() < amountInCents) {
            log.warn("⚠️ Insufficient funds in accountId={}. Requested={}, Available={}",
                    fromAccountId, amountInCents, from.getBalanceInCents());
            throw new InsufficientFundsException("Insufficient funds");
        }

        from.setBalanceInCents(from.getBalanceInCents() - amountInCents);
        to.setBalanceInCents(to.getBalanceInCents() + amountInCents);

        accountRepository.save(from);
        accountRepository.save(to);

        log.info("✅ Transfer completed successfully: fromAccountId={}, toAccountId={}, amountInCents={}", fromAccountId, toAccountId, amountInCents);
    }
}