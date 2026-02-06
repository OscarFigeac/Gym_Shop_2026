package org.example.gym_shop_2026.mvc;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * An MVC controller for handling redirects to named views.
 *
 * @author Cal Woods
 */
@Controller
public class MVController {
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
}