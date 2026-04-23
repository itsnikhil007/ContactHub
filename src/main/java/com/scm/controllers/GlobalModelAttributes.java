package com.scm.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalModelAttributes {

    @ModelAttribute
    public void addGlobalAttributes(HttpServletRequest request, org.springframework.ui.Model model) {
        model.addAttribute("currentPath", request.getRequestURI());
    }
}