package org.example.gym_shop_2026.entities;

import lombok.*;

/**
 * An entity representing subscription records in subscriptions persistence.
 *
 * @author Cal Woods
 */
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class Subscription {
    @Setter(AccessLevel.NONE)
    @EqualsAndHashCode.Include private int planId = 1;

    @NonNull private String planName;
    @NonNull private String description;
    @NonNull private double planPrice;
    @NonNull private int planDuration;
}