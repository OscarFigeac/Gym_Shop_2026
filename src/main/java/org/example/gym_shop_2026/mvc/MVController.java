package org.example.gym_shop_2026.mvc;

import org.example.gym_shop_2026.services.TwoFactorAuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * An MVC controller for handling redirects to named views.
 *
 * @author Cal Woods
 */
@Controller
public class MVController {

    private final TwoFactorAuthenticationService tfaService;

    @Autowired
    public MVController(TwoFactorAuthenticationService tfaService) {
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

    @PostMapping("/confirm-2fa")
    public String confirm2fa(@RequestParam("code") int code,
                             @RequestParam("secret") String secret,
                             java.security.Principal principal){

        String username = principal.getName();

        if (tfaService.verifyToken(username, secret, code)){
            tfaService.finalize2faSetup(username, secret);
            return "redirect:/?mfa_success=true";
        }

        return "redirect:/register?error=invalid_2fa";
    }
}