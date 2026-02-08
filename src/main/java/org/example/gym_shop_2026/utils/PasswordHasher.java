package org.example.gym_shop_2026.utils;



public class PasswordHasher {
    private static final int WORKLOAD = 12;

    public static String hashPassword(String plainText){
        if(plainText == null || plainText.isEmpty()){
            throw new IllegalArgumentException("Unable to hash a password that has a NULL value or is empty !");
        }

        String salt = BCrypt.gensalt(WORKLOAD);
        return BCrypt.hashpw(plainText, salt);
    }

    public static boolean verifyPassword(String plainText, String hashedPassword){
        if(plainText == null || hashedPassword == null || hashedPassword.isEmpty()){
            return false;
        }

        return BCrypt.checkpw(plainText, hashedPassword);
    }
}
