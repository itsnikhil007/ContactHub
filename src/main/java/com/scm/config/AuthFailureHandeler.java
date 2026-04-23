package com.scm.config;

import com.scm.helpers.Message;
import com.scm.helpers.MessageType;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.io.IOException;

@Component
public class AuthFailureHandeler implements AuthenticationFailureHandler {


    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {

        if(exception instanceof DisabledException) {

            HttpSession session = request.getSession();
            session.setAttribute("message", Message.builder().content("Your account is not verified yet. Please check your email for the verification link.").type(MessageType.red).build());

            response.sendRedirect("/login");
        }else{
            response.sendRedirect("/login?error=true");
        }
    }
}
