package org.example.gym_shop_2026.security;

import lombok.extern.slf4j.Slf4j;
import org.example.gym_shop_2026.entities.User;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.example.gym_shop_2026.services.TwoFactorAuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CustomAuthenticationProvider extends DaoAuthenticationProvider {

    @Autowired
    private TwoFactorAuthenticationService tfaService;

    public CustomAuthenticationProvider(UserDetailsService userDetailsService) {
        super(userDetailsService);
    }

    /**
     * @author Oscar
     * Retrieves the user's information from the database for authentication. Verifies
     * that the user has enabled their 2FA and performs the authentication using the
     * unique key stored in the database.
     * @param auth The user's data.
     * @return The result whether it was successfully logged in or not.
     * @throws BadCredentialsException if any of the input is wrong.
     */
    @Override
    public Authentication authenticate(Authentication auth){
         Authentication result = super.authenticate(auth);

         CustomWebAuthenticationDetails details = (CustomWebAuthenticationDetails) auth.getDetails();
         String verificationCode = details.getVerificationCode();

         User user = (User) result.getPrincipal();

         if (!user.is2faEnabled()){
             log.info("2FA is not enabled for User: {}", user.getUsername());
             return result;
         }

         String userSecret = user.getSecretKey();

         //is it a BadCredentials Exception?
         if (verificationCode == null || verificationCode.trim().isEmpty()){
             throw new BadCredentialsException("2FA is enabled for this user. Please use your authenticator to access your account.");
         }

         try{
             int code = Integer.parseInt(verificationCode.replaceAll("\\s+", ""));

             if (!tfaService.verifyToken(user.getUsername(), userSecret, code)){
                 throw new BadCredentialsException("The code is incorrect or has expired.");
             }
         } catch (NumberFormatException e){
             throw new BadCredentialsException("Invalid format. Please enter a 6 digit code.");
         }

         log.info("User: {} fully authenticated. Starting session." , user.getUsername());
         return result;
    }
}
