package com.example.BankSystem.model;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Entity
@Table(name = "account")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String owner;
    @Column(name = "balance_in_cents")
    private Long balanceInCents;


    @Transient
    public BigDecimal getBalanceInAzn() {
        if (balanceInCents == null) {
            return BigDecimal.ZERO;
        }
        return BigDecimal.valueOf(balanceInCents)
                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
    }

    public void setBalanceInAzn(BigDecimal balanceInAzn) {
        if (balanceInAzn != null) {
            this.balanceInCents = balanceInAzn.multiply(BigDecimal.valueOf(100)).longValue();
        } else {
            this.balanceInCents = 0L;
        }
    }
}
