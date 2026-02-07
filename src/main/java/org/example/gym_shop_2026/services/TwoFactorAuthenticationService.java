package org.example.gym_shop_2026.services;

import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorConfig;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class TwoFactorAuthenticationService {

    private final GoogleAuthenticator googleAuthenticator;

    public TwoFactorAuthenticationService(){
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
    public GoogleAuthenticatorKey createCredentials(){
        return googleAuthenticator.createCredentials();
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
    public String generateToken(String username){
        log.info("Generating new 2FA token for user {} ", username );
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
    public boolean verifyToken(String username, String token, int code){
        log.debug("Verification attempt for user: {} code: {}" , username, code);

        boolean isValid = googleAuthenticator.authorize(token, code);

        if (isValid){
            log.info("2FA SUCCESS: User: {} has successfully logged in. ", username);
        } else {
            log.warn("2FA ALERT: User: {} failed to provide code. ", username);
        }

        return isValid;
    }
}
