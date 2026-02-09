package org.example.gym_shop_2026.services;

import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorConfig;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import com.warrenstrange.googleauth.GoogleAuthenticatorQRGenerator;
import lombok.extern.slf4j.Slf4j;
import org.example.gym_shop_2026.entities.User;
import org.example.gym_shop_2026.persistence.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class TwoFactorAuthenticationService {

    private final GoogleAuthenticator googleAuthenticator;
    private final UserDAO userDAO;

    @Autowired
    public TwoFactorAuthenticationService(UserDAO userDAO) {
        this.userDAO = userDAO;
        // here we add a one-minute timer, as it's set to 30 seconds by default, which
        // could result in the user having trouble to log in.
        // the risk of breaches using past keys does increase. to be discussed.

        GoogleAuthenticatorConfig config = new GoogleAuthenticatorConfig.GoogleAuthenticatorConfigBuilder()
                .setTimeStepSizeInMillis(TimeUnit.MINUTES.toMillis(1))
                .setWindowSize(3) // allows a one-minute search through new and old tokens. Three stands for 1 in the past, 1 in the present and 1 in the future
                .build();

        this.googleAuthenticator = new GoogleAuthenticator(config);
        log.info("2FA service initialised with a 60-second timer.");
    }

    /**
     * @author Oscar
     * Creates login credentials for the user.
     * @return A unique login credential for each user.
     */
    public GoogleAuthenticatorKey createCredentials() {
        return googleAuthenticator.createCredentials();
    }

    /**
     * @author Oscar
     * Generates a URL that'll be turned into a QR on our frontend.
     * @param username The user's username.
     * @param key The object that contains the secret key.
     * @return A URL String object starting with 'otpauth://'
     */
    public String generateQrCodeUrl(String username, GoogleAuthenticatorKey key) {
        log.info("Generating QR code for User: {}", username);
        String baseUrl = GoogleAuthenticatorQRGenerator.getOtpAuthURL("GymShop", username, key);

        // This is basically telling the user end to only refresh every 60 seconds
        return baseUrl + "&period=60";
    }

    /**
     * @author Oscar
     * Takes in a username and generates a unique login token for them using
     * the Google Authentication Library.
     * @param username The user the key is being generated for.
     * @return The token to keep track in the database.It also serves as an anchor
     *         between the user's end and the server to agree on what the code should
     *         be.
     */
    public String generateToken(String username) {
        log.info("Generating new 2FA token for user {} ", username);
        final GoogleAuthenticatorKey key = googleAuthenticator.createCredentials();

        return key.getKey();
    }

    /**
     * @author Oscar
     * Takes in a username, token and code, and using the Google Authentication Library
     * it authorises the username to log in if they pass the authentication process.
     * @param username The user attempting to log in.
     * @param token The token to keep track in the database.It also serves as an anchor
     *              between the user's end and the server to agree on what the code should
     *              be.
     * @param code The number the user will see and enter to verify its identity.
     * @return True if success, false otherwise.
     */
    public boolean verifyToken(String username, String token, int code) {
        log.debug("Verification attempt for user: {} code: {}", username, code);

        boolean isValid = googleAuthenticator.authorize(token, code);

        if (isValid) {
            log.info("2FA SUCCESS: User: {} has successfully logged in. ", username);
        } else {
            log.warn("2FA ALERT: User: {} failed to provide code. ", username);
        }

        return isValid;
    }

    /**
     * @author Oscar
     * Sets up the 2FA flag to true after completing setup.
     * @param username The user completing the setup.
     * @param generatedSecret The new unique token generated for the user.
     * @throws SQLException If anything goes wrong with the database at any point.
     */
    public void finalize2faSetup(String username, String generatedSecret) throws SQLException {
        log.info("Finalising setup for User: {}", username);
        User existingUser = (User) userDAO; //casting it? will it work???
        if (existingUser != null) {
            User updatedUser = User.builder()
                    .user_id(existingUser.getUser_id())
                    .username(existingUser.getUsername())
                    .fullName(existingUser.getFullName())
                    .userType(existingUser.getUserType())
                    .email(existingUser.getEmail())
                    .password(existingUser.getPassword())
                    .dob(existingUser.getDob())
                    .secretKey(generatedSecret)
                    .is2faEnabled(true)
                    .build();
            userDAO.updateUser(updatedUser);
        }
    }

    /**
     * Generates a QR Image Google Authenticator can understand
     * @param token the unique token stored in the database for each user
     * @param username the user the image is being generated for
     * @return
     */
    public String generateQrCodeImageUrl(String token, String username) {
        // creates a URL that Google Authenticator can understand
        return "https://api.qrserver.com/v1/create-qr-code/?size=200x200&data=" +
                "otpauth://totp/GymShop:" + username + "?secret=" + token + "&issuer=GymShop";
    }
}
