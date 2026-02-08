package org.example.gym_shop_2026.entities;

import lombok.*;


import java.sql.Time;
import java.util.Date;
import java.util.Objects;

@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
@Getter
@Builder
@AllArgsConstructor
public class Users {
    @EqualsAndHashCode.Include
    @NonNull
    private int user_id;
    @NonNull
    private String username;
    @NonNull
    private String fullName;
    @NonNull
    private String userType;
    @NonNull
    private String email;
    @NonNull
    private String password;
    @NonNull
    private Date dob;
}
