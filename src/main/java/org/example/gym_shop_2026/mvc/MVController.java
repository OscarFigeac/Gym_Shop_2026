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

import java.sql.Date;
import java.sql.SQLException;

/**
 * An MVC controller for handling redirects to named views.
 *
 * @author Oscar Figeac & Cal Woods
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

    @PostMapping("/register")
    public String registerUser(@RequestParam String fName,
                               @RequestParam String lName,
                               @RequestParam String eMail,
                               @RequestParam String username,
                               @RequestParam String password,
                               @RequestParam(required = false) String dob,
                               Model model) throws SQLException {

        //converting html date into java.sql.date for dao readability
        Date sqlDate = (dob != null && !dob.isEmpty())
                ? Date.valueOf(dob)
                : new Date(System.currentTimeMillis());

        boolean isRegistered = userDAO.register(username, fName + " " +
                lName, "customer", eMail, password, sqlDate);

        if (isRegistered){
            String secret = tfaService.generateToken(username);
            String qrCodeUrl = tfaService.generateQrCodeImageUri(secret, username);

            model.addAttribute("username", username);
            model.addAttribute("secret", secret);
            model.addAttribute("qrCodeUrl", qrCodeUrl);

            return "setup-2fa";
        }

        return "redirect:/register?error=registration_failed";
    }

    /**
     * @author Oscar
     * Looks for a tag named code within the login.html file to trigger the authentication process.
     *
     * @param code The 6-digit code to authenticate.
     * @param secret The unique String token.
     * @param username The user being authenticated.
     * @return a String to redirect the user to whether a success page or an invalid one.
     */
    @PostMapping("/confirm-2fa")
    public String confirm2fa(@RequestParam("code") int code,
                             @RequestParam("secret") String secret,
                             @RequestParam("username") String username) throws SQLException {

        if (tfaService.verifyToken(username, secret, code)){
            tfaService.finalize2faSetup(username, secret);

            return "redirect:/login?registered=true";
        }

        return "redirect:/setup-2fa?error=invalid_code&username=" + username + "&secret=" + secret;
    }



}