package com.eris.gitlabanalyzer.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;


@RestController
public class AuthController {
    @Value("${FRONTEND_URL}")
    private String FRONTEND_URL;

    // TODO: Homepage redirects to frontend so that we know the backend is running when Brian is marking
    // We can remove this once we deploy to the VM
    @GetMapping("/")
    public ModelAndView method() {
        return new ModelAndView("redirect:" + FRONTEND_URL);
    }

    @GetMapping("/api/v1/login")
    public String login() {
        // TODO: Delete once we have SSO set up
        // Because we are currently using basic auth, this will only successfully execute if the request has valid auth headers
        return "Success";
    }
}
