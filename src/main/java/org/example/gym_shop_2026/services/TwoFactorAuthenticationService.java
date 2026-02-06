package org.example.gym_shop_2026.services;

import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorConfig;
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
                .setTimeStepSizeInMillis(TimeUnit.MILLISECONDS.toMillis(1))
                .setWindowSize(3) // allows a one-minute search through new and old tokens. Three stands for 1 in the past, 1 in the present and 1 in the future
                .build();

        this.googleAuthenticator = new GoogleAuthenticator(config);
        log.info("2FA service initialised with a 60-second timer.");
    }
}
