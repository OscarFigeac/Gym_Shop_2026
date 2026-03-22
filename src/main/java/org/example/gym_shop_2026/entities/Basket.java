package org.example.gym_shop_2026.entities;

import lombok.*;

import java.sql.Timestamp;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Basket {
    @EqualsAndHashCode.Include
    private final int basketId;
    @NonNull
    private final int user_id;
    @NonNull
    private String status; //Might need to be changed to enum ('Active', 'Ordered', 'Dismissed')
    @NonNull
    private Timestamp createdAt;

    private List<BasketItem> items;
}
