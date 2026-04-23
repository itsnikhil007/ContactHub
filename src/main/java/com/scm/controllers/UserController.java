package com.scm.controllers;

import com.scm.entities.User;
import com.scm.helpers.Helper;
import com.scm.services.ContactService;
import com.scm.services.UserService;
import com.scm.services.impl.UserServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.thymeleaf.model.IModel;


@Controller
@RequestMapping("/user")
public class UserController {

    Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;
    @Autowired
    private ContactService contactService;

    //user dashboard page

    @GetMapping("/dashboard")
    public String userDashboard(){
        return "/user/dashboard";
    }

    //user profile page

    @GetMapping("/profile")
    public String userProfile(Model model, Authentication authentication) {

        String emailOfLoggedInUser = Helper.getEmailOfLoggedInUser(authentication);
        int userContactCount = contactService.getContactCountByUserId(emailOfLoggedInUser).size();

        String authProvider = Helper.getAuthProvider(authentication);

        model.addAttribute("userContactCount", userContactCount);
        model.addAttribute("authProvider", authProvider);

        return "user/profile";
    }

    @GetMapping("/messages")
    public String userMessage(){
        return "/user/messages";
    }


    //user add contacts page

    //user view contacts

    //user edit contact

    //user delete contact

    //user search contact
}
