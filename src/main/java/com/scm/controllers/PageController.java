package com.scm.controllers;

import com.scm.entities.User;
import com.scm.forms.UserForm;
import com.scm.helpers.Message;
import com.scm.helpers.MessageType;
import com.scm.services.UserService;
import com.scm.services.exceptions.DuplicateEmailException;
import com.scm.services.exceptions.VerificationEmailException;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class PageController {

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String index(){

        return "redirect:/home";
    }

    @GetMapping("/home")
    public String home(Model model){
        System.out.println("Home page handler");
        model.addAttribute("name", "Substring technologies");
        model.addAttribute("youtubeChannel", "@nikhilsingh005");
        model.addAttribute("gitrepo", "https://github.com/itsnikhil007");

        return "home";
    }

    //about route
    @GetMapping("/about")
    public String aboutPage(Model model){
        model.addAttribute("isLogin", false);
        System.out.println("About page loading");
        return "about";
    }

    //services page
    @GetMapping("/services")
    public String servicesPage(){
        System.out.println("services page loading");
        return "services";
    }

    //contact page
    @GetMapping("/contact")
    public String contactPage(){
        System.out.println("Contact page loading");
        return "contact";
    }

    //login page
    @GetMapping("/login")
    public String loginPage(){
        System.out.println("Login page loading");
        return "login";
    }

    //register page
    @GetMapping("/register")
    public String registerPage(Model model){
        System.out.println("Register page loading");

        UserForm userForm = new UserForm();

        model.addAttribute("userForm", userForm);
        return "register";
    }

    @PostMapping("/do-register")
    public String processRegister(@Valid @ModelAttribute UserForm userForm, BindingResult rBindingResult, HttpSession session){
        if(rBindingResult.hasErrors()){
            return "register";
        }

        User user = new User();
        user.setName(userForm.getName());
        user.setEmail(userForm.getEmail());
        user.setPassword(userForm.getPassword());
        user.setAbout(userForm.getAbout());
        user.setPhoneNumber(userForm.getPhoneNumber());
        user.setEnabled(false);
        user.setProfilePic("https://assets.leetcode.com/users/Nikhilsingh24/avatar_1770964654.png");
        try {
            userService.registerUser(user);
            Message message = Message.builder()
                    .content("Registration successful. Please check your email to verify your account.")
                    .type(MessageType.green)
                    .build();

            session.setAttribute("message", message);
            return "redirect:/register";
        } catch (DuplicateEmailException ex) {
            rBindingResult.rejectValue("email", "email.duplicate", ex.getMessage());
            return "register";
        } catch (VerificationEmailException ex) {
            session.setAttribute("message", Message.builder()
                    .content(ex.getMessage())
                    .type(MessageType.red)
                    .build());
            return "redirect:/register";
        }

    }
}
