package org.example.gym_shop_2026.entities;

import lombok.*;
import jakarta.validation.constraints.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Product {

    @EqualsAndHashCode.Include
    private Integer productId;

    @NotBlank(message = "Category is required")
    @NonNull
    private String productCategory;

    @NotBlank(message = "Product name is required")
    @NonNull
    private String name;

    @NotBlank(message = "Description is required")
    @NonNull
    private String description;

    @Min(value = 0, message = "In stock quantity cannot be negative")
    @NonNull
    private int quantity;

    @Min(value = 0, message = "Price cannot be negative")
    @NonNull
    private double price;

    private String imageUrl;

    private int total_sold;
}
