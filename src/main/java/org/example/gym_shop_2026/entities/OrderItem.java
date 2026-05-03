package org.example.gym_shop_2026.entities;

import lombok.*;

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderItem {
    @NonNull
    private int item_id;
    @NonNull
    private int order_id;
    @NonNull
    private int product_id;
    @NonNull
    private int quantity;
    @NonNull
    private double priceAtPurchase;
    private Product product;
}
