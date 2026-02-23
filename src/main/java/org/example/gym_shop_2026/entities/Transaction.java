package org.example.gym_shop_2026.entities;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalTime;

@Slf4j
@Getter
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
public class Transaction {
    @EqualsAndHashCode.Include
    private int transactionId;

    private int userId;
    private int planId;
    private int methodId;
    private double amountPaid;
    private LocalTime transactionDate;

    @Setter
    private String status;
}