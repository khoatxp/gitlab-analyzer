package com.eris.gitlabanalyzer.controller;

import com.eris.gitlabanalyzer.model.User;
import com.eris.gitlabanalyzer.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;

@RestController
public class AuthController {

    private final AuthService authService;

    @Value("${FRONTEND_URL}")
    private String FRONTEND_URL;

    @Autowired
    public AuthController(AuthService authService){
        this.authService = authService;
    }

    // TODO: Homepage redirects to frontend so that we know the backend is running when Brian is marking
    // We can remove this once we deploy to the VM
    @GetMapping("/")
    public ModelAndView redirect() {
        return new ModelAndView("redirect:" + FRONTEND_URL);
    }

    @GetMapping("/api/v1/user")
    public User getLoggedInUser(Principal currentUser) {
        return authService.getLoggedInUser(currentUser);
    }
}
