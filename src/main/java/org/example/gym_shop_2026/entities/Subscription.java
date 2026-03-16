package org.example.gym_shop_2026.entities;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

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
@Slf4j
public class Subscription implements Comparable<Subscription> {
    @Setter(AccessLevel.NONE)
    private int planId = 1;

    @NonNull @EqualsAndHashCode.Include private String planName;
    @NonNull private String description;
    @NonNull private double planPrice;
    @NonNull private int planDuration;

    @Override
    public int compareTo(Subscription o) {
        if(o == null) {
            log.error("Could not compare subscriptions aa given Subscription object o was null!");
            throw new  IllegalArgumentException("Given Subscription object o was null");
        }

        if(this.equals(o)) {
            return 0;
        }

        return this.planName.compareTo(o.planName);
    }
}