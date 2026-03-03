package org.example.gym_shop_2026.entities;

import lombok.*;
import jakarta.validation.constraints.*;

@Getter
@Builder
@AllArgsConstructor
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Product {

    @EqualsAndHashCode.Include
    private final int productId;

    @NotBlank(message = "Category is required")
    @NonNull
    private final String productCategory;

    @NotBlank(message = "Product name is required")
    @NonNull
    private final String name;

    @NotBlank(message = "Description is required")
    @NonNull
    private final String description;

    @Min(value = 0, message = "In stock quantity cannot be negative")
    @NonNull
    private final int inStock;

    private final int storeId;
}
