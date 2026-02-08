package org.example.gym_shop_2026.entities;

import lombok.*;


import java.util.Date;

@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
@Getter
@Builder
@AllArgsConstructor
public class User {
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
    //2FA
    @NonNull
    private String secretKey;
    @NonNull
    private boolean is2faEnabled;
}
