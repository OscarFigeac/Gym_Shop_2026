package org.example.gym_shop_2026.entities;

import lombok.*;


import java.util.Date;

@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
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
    @NonNull //change to enum
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

    @NonNull
    public int getUser_id() {
        return user_id;
    }

    public @NonNull String getUsername() {
        return username;
    }

    public @NonNull String getFullName() {
        return fullName;
    }

    public @NonNull String getUserType() {
        return userType;
    }

    public @NonNull String getEmail() {
        return email;
    }

    public @NonNull String getPassword() {
        return password;
    }

    public @NonNull Date getDob() {
        return dob;
    }

    public @NonNull String getSecretKey() {
        return secretKey;
    }

    @NonNull
    public boolean is2faEnabled() {
        return is2faEnabled;
    }
}
