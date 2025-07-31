package com.example.BankSystem.controller;

import com.example.BankSystem.model.Account;
import com.example.BankSystem.repository.AccountRepository;
import com.example.BankSystem.service.AccountService;
import com.example.BankSystem.service.TransferService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountRepository accountRepository;
    private final TransferService transferService;

    @PostMapping
    public ResponseEntity<Account> createAccount(@RequestBody Account account) {
        Account saved = accountRepository.save(account);
        return ResponseEntity.ok(saved);
    }

    @GetMapping
    public List<Account> getAll() {
        return accountRepository.findAll();
    }

    @PostMapping("/transfer")
    public ResponseEntity<String> transfer(@RequestParam Long fromId,
                                           @RequestParam Long toId,
                                           @RequestParam long amountInCents) {
        try {
            transferService.transfer(fromId, toId, amountInCents);
            return ResponseEntity.ok("Transfer successful");
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }

    }
}
