package org.example.gym_shop_2026.entities;

import lombok.*;

import java.util.Objects;

@Getter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Builder
@AllArgsConstructor
public class paymentMethod {

    @EqualsAndHashCode.Include
    private int methodId;
    @NonNull
    private int userId;
    @NonNull
    private String processorToken;
    @NonNull
    private int lastFourDigits;
    @NonNull
    private String expiryDate;
    @NonNull
    private String cardType;
    @NonNull
    private boolean isValid;
    @NonNull
    private boolean isPrimary;

    private String format(){
        return methodId + ": " +
                "\n\tUserID: " + userId +
                "\n\tProcessor Token: " + processorToken +
                "\n\tLast Four Digits: " + lastFourDigits +
                "\n\tExpiry Date: " + expiryDate +
                "\n\tCard Type: " + cardType +
                "\n\t Valid Payment: " + isValid +
                "\n\tPrimary Payment Method: " + isPrimary;
    }

    /**
     * Checks that every field of the objects is the same to the one being pulled from the database.
     * @param p1 The first instance being compared to p2
     * @param p2 The second instance being used to be compared to p1
     * @return true if equals, false otherwise
     */
    public static boolean deepEquals(paymentMethod p1, paymentMethod p2){
        return Objects.equals(p1.methodId, p2.methodId)
                && Objects.equals(p1.userId, p2.userId)
                && Objects.equals(p1.processorToken, p2.processorToken)
                && Objects.equals(p1.lastFourDigits, p2.lastFourDigits)
                && Objects.equals(p1.expiryDate, p2.expiryDate)
                && Objects.equals(p1.cardType, p2.cardType)
                && Objects.equals(p1.isValid, p2.isValid)
                && Objects.equals(p1.isPrimary, p2.isPrimary);
    }
}
