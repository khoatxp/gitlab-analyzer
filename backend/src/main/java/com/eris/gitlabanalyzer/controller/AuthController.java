package com.eris.gitlabanalyzer.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class AuthController {

    @GetMapping("/api/v1/login")
    public String login() {
        // TODO: Delete once we have SSO set up
        // Because we are currently using basic auth, this will only successfully execute if the request has valid auth headers
        return "Success";
    }
}
