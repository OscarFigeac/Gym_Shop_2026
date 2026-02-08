package org.example.gym_shop_2026.security;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

public class CustomWebAuthenticationDetails extends WebAuthenticationDetails {

    private final String verificationCode;

    public CustomWebAuthenticationDetails(HttpServletRequest request){
        super(request);
        //What this is doing is looking for an <input name="code"> field in the login.html file.
        this.verificationCode = request.getParameter("code");
    }

    public String getVerificationCode(){
        return verificationCode;
    }
}
