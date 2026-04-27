package org.example.gym_shop_2026.entities;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class BasketItem {
    @EqualsAndHashCode.Include
    private final int itemId;
    @NonNull
    private int productId;
    @NonNull
    private int basketId;
    @NonNull
    private int itemQuantity;
    private String productName;
    private double price;
    private double subtotal;
    private Product product;
}