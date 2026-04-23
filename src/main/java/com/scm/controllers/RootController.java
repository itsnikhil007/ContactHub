package com.scm.controllers;

import com.scm.entities.User;
import com.scm.helpers.Helper;
import com.scm.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class RootController {

    Logger logger = LoggerFactory.getLogger(RootController.class);

    @Autowired
    private UserService userService;

    @ModelAttribute
    public void addLoggedInUserInformation(Model model, Authentication authentication) {

        if(authentication == null || !authentication.isAuthenticated()) {
            return;
        }

        String username = Helper.getEmailOfLoggedInUser(authentication);
        if (username == null || username.isBlank()) {
            return;
        }

        logger.info("user profile -> " + username);

        User user = userService.getUserByEmail(username);
        if (user == null) {
            logger.warn("No local user found for authenticated principal: {}", username);
            model.addAttribute("loggedInUser", null);
            return;
        }

        model.addAttribute("loggedInUser", user);
    }
}
