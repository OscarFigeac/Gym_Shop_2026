package org.example.gym_shop_2026.entities;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    @NonNull
    private int order_id;
    @NonNull
    private int user_id;
    @NonNull
    private double totalAmount;
    private LocalDateTime orderDate;
    private String status;
    private List<OrderItem> items;
}
