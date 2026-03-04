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
public class Subscription {
    @Setter(AccessLevel.NONE)
    @EqualsAndHashCode.Include private int planId;

    @NonNull private String description;
    @NonNull private String planName;
    @NonNull private double planPrice;
    @NonNull private int planDuration;
}