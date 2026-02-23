package org.example.gym_shop_2026.security;

import lombok.extern.slf4j.Slf4j;
import org.example.gym_shop_2026.entities.User;
import org.example.gym_shop_2026.persistence.UserDAO;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.example.gym_shop_2026.services.TwoFactorAuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.sql.SQLException;

@Slf4j
@Component
public class CustomAuthenticationProvider extends DaoAuthenticationProvider {

    private TwoFactorAuthenticationService tfaService;
    private UserDAO userDAO;

    @Autowired
    public CustomAuthenticationProvider(UserDetailsServiceImpl userDetailsService,
                                        TwoFactorAuthenticationService tfaService,
                                        UserDAO userDAO) {
        super(userDetailsService);
        this.tfaService = tfaService;
        this.userDAO = userDAO;
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

         org.springframework.security.core.userdetails.User springUser =
                 (org.springframework.security.core.userdetails.User) result.getPrincipal();

         org.example.gym_shop_2026.entities.User dbUser;

         try{
             dbUser = userDAO.findByUsername(springUser.getUsername());
         } catch (SQLException e) {
             throw new BadCredentialsException("Security Database Error");
         }

         if (!dbUser.is2faEnabled()){
             log.info("2FA is not enabled for User: {}", dbUser.getUsername());
             return result;
         }

        CustomWebAuthenticationDetails details = (CustomWebAuthenticationDetails) auth.getDetails();
        String verificationCode = details.getVerificationCode();

        if (verificationCode == null || verificationCode.trim().isEmpty()){
            throw new BadCredentialsException("2FA Required");
        }

        int code = Integer.parseInt(verificationCode.replaceAll("\\s+", ""));
        if (!tfaService.verifyToken(dbUser.getUsername(), dbUser.getSecretKey(), code)) {
            throw new BadCredentialsException("Incorrect 2FA Code");
        }

        return result;
    }
}
