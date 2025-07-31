package com.example.BankSystem.repository;

import com.example.BankSystem.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {

}
