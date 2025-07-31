package com.example.BankSystem.controller;

import com.example.BankSystem.dto.TransferRequest;
import com.example.BankSystem.model.Account;
import com.example.BankSystem.service.AccountService;
import com.example.BankSystem.service.TransferService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/accounts")
@RequiredArgsConstructor
public class WebAccountController {
    private final AccountService accountService;
    private final TransferService transferService;


    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("account", new TransferRequest());
        return "createAccount";
    }

    @PostMapping("/create")
    public String createAccount(@ModelAttribute Account account) {
        accountService.createAccount(account);
        return "redirect:/accounts/list";
    }


    @GetMapping("/list")
    public String listAccounts(Model model) {
        model.addAttribute("accounts", accountService.getAllAccounts());
        return "accountList";
    }

    // Transfer formu göstər
    @GetMapping("/transfer")
    public String showTransferForm(Model model) {
        model.addAttribute("transferRequest", new TransferRequest());
        return "transferForm";
    }

    @PostMapping("/transfer")
    public String handleTransfer(@ModelAttribute TransferRequest transferRequest, Model model) {
        try {
            long amountInCents = Math.round(transferRequest.getAmountInAzn() * 100);
            accountService.transfer(
                    transferRequest.getFromAccountId(),
                    transferRequest.getToAccountId(),
                    amountInCents
            );

            Account fromAccount = accountService.getAccountById(transferRequest.getFromAccountId());
            Account toAccount = accountService.getAccountById(transferRequest.getToAccountId());

            model.addAttribute("message", "Transfer successful");
            model.addAttribute("fromAccount", fromAccount);
            model.addAttribute("toAccount", toAccount);

        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
        }
        return "transferForm";
    }

}