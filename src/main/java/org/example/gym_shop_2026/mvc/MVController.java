package org.example.gym_shop_2026.mvc;

import org.example.gym_shop_2026.entities.User;
import org.example.gym_shop_2026.persistence.UserDAO;
import org.example.gym_shop_2026.services.TwoFactorAuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.SQLException;

/**
 * An MVC controller for handling redirects to named views.
 *
 * @author Cal Woods
 */
@Controller
public class MVController {

    private final PasswordEncoder passwordEncoder;
    private final UserDAO userDAO;
    private final TwoFactorAuthenticationService tfaService;

    @Autowired
    public MVController(PasswordEncoder passwordEncoder, UserDAO userDAO, TwoFactorAuthenticationService tfaService) {
        this.passwordEncoder = passwordEncoder;
        this.userDAO = userDAO;
        this.tfaService = tfaService;
    }

    /**
     * Loads index.html page when get requesting url /.
     *
     * @return A {@link String} representing a view index.html web page.
     */
    @GetMapping(path = "/")
    public String index() {
        return "index";
    }

    /**
     * Loads login.html page when get requesting /login.
     *
     * @return A {@link String} representing a view login.html web page
     */
    @GetMapping(path = "/login")
    public String login() {
        return "login";
    }

    /**
     * Loads register.html page when get requesting /register.
     *
     * @return A {@link String} representing a view register.html web page
     */
    @GetMapping(path = "/register")
    public String register() {
        return "register";
    }

    /**
     * @author Oscar
     * Looks for a tag named code within the login.html file to trigger the authentication process.
     *
     * @param code The 6-digit code to authenticate.
     * @param secret The unique String token.
     * @param principal The User object.
     * @return a String to redirect the user to whether a success page or an invalid one.
     */
    @PostMapping("/confirm-2fa")
    public String confirm2fa(@RequestParam("code") int code,
                             @RequestParam("secret") String secret,
                             java.security.Principal principal) throws SQLException {

        String username = principal.getName();

        if (tfaService.verifyToken(username, secret, code)){
            tfaService.finalize2faSetup(username, secret);
            return "redirect:/?mfa_success=true";
        }

        return "redirect:/register?error=invalid_2fa";
    }

    @PostMapping("/register")
    public String registerUser(@RequestParam String fName,
                               @RequestParam String lName,
                               @RequestParam String eMail,
                               @RequestParam String username,
                               @RequestParam String password,
                               Model model) throws SQLException {

        String encodedPassword = passwordEncoder.encode(password);

        User newUser = User.builder()
                .username(username)
                .fullName(fName + " " + lName)
                .email(eMail)
                .password(encodedPassword)
                .userType("customer")
                .is2faEnabled(false)
                .build();

        userDAO.updateUser(newUser); //HAVE A LOOK AT THIS. MIGHT NEED A SAVE METHOD INSTEAD OF USING THE UPDATE ONE

        String secret = tfaService.generateToken(username);
        String qrCodeUrl = tfaService.generateQrCodeImageUrl(secret, username);

        model.addAttribute("username", username);
        model.addAttribute("secret", secret);
        model.addAttribute("qrCodeUrl", qrCodeUrl);

        return "setup-2fa";
    }
}