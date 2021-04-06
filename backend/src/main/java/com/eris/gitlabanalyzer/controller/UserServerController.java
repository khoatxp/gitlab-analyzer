package com.eris.gitlabanalyzer.controller;

import com.eris.gitlabanalyzer.service.AuthService;
import com.eris.gitlabanalyzer.service.UserServerService;
import com.eris.gitlabanalyzer.viewmodel.UserServerRequestBody;
import com.eris.gitlabanalyzer.viewmodel.UserServerView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.stream.Stream;

@RestController
@RequestMapping(path = "/api/v1/servers")
public class UserServerController {
    private final AuthService authService;
    private final UserServerService userServerService;

    @Autowired
    public UserServerController(AuthService authService, UserServerService userServerService){
        this.authService = authService;
        this.userServerService = userServerService;
    }

    @GetMapping
    public Stream<UserServerView> getUserServers(Principal principal) {
        var user =  authService.getLoggedInUser(principal);
        var userServers = userServerService.getUserServers(user);
        return userServers.stream().map(UserServerView::fromUserServer);
    }

    @GetMapping(path ="/{serverId}")
    public UserServerView getUserServer(Principal principal, @PathVariable("serverId") Long serverId) {
        var user = authService.getLoggedInUser(principal);
        var userServer = userServerService.getUserServer(user, serverId);
        var server = userServer.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find server."));
        return UserServerView.fromUserServer(server);
    }

    @PostMapping
    public UserServerView addNewUserServer(Principal principal, @RequestBody UserServerRequestBody requestBody) {
        var user = authService.getLoggedInUser(principal);
        var userServer = userServerService.createUserServer(user, requestBody.getServerUrl(), requestBody.getAccessToken());
        return UserServerView.fromUserServer(userServer);
    }

    @PutMapping(path = "/{serverId}")
    public UserServerView updateUserServer(
            Principal principal,
            @PathVariable("serverId") Long serverId,
            @RequestBody String accessToken){
        var user = authService.getLoggedInUser(principal);
        var userServer = userServerService.updateUserServer(user, serverId, accessToken);
        return UserServerView.fromUserServer(userServer);
    }

    @DeleteMapping(path = "/{serverId}")
    public Long deleteUserServer(Principal principal, @PathVariable("serverId") Long serverId) {
        var user = authService.getLoggedInUser(principal);
        userServerService.deleteUserServer(user, serverId);
        return serverId;
    }

}
