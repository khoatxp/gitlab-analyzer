package com.eris.gitlabanalyzer.controller;

import com.eris.gitlabanalyzer.model.User;
import com.eris.gitlabanalyzer.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.nio.file.AccessDeniedException;
import java.security.Principal;

@RestController
public class AuthController {

    private final UserRepository userRepository;

    @Value("${FRONTEND_URL}")
    private String FRONTEND_URL;

    @Autowired
    public AuthController(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    // TODO: Homepage redirects to frontend so that we know the backend is running when Brian is marking
    // We can remove this once we deploy to the VM
    @GetMapping("/")
    public ModelAndView redirect() {
        return new ModelAndView("redirect:" + FRONTEND_URL);
    }

    @GetMapping("/api/v1/user")
    public User getLoggedInUser(Principal currentUser) throws AccessDeniedException {
        if (currentUser == null) {
            throw new AccessDeniedException("User not logged in.");
        }
        return userRepository.findUserByUsername(currentUser.getName()).orElseThrow(() -> new AccessDeniedException("User not found."));
    }
}
