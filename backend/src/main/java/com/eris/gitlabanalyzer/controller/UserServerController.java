package com.eris.gitlabanalyzer.controller;

import com.eris.gitlabanalyzer.repository.UserRepository;
import com.eris.gitlabanalyzer.service.UserServerService;
import com.eris.gitlabanalyzer.viewmodel.UserServerRequestBody;
import com.eris.gitlabanalyzer.viewmodel.UserServerView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.Optional;
import java.util.stream.Stream;

@RestController
@RequestMapping(path = "/api/v1/servers")
public class UserServerController {
    private final UserRepository userRepository;
    private final UserServerService userServerService;

    @Autowired
    public UserServerController(UserRepository userRepository, UserServerService userServerService){
        this.userRepository = userRepository;
        this.userServerService = userServerService;
    }

    @GetMapping
    public Stream<UserServerView> getUserServers(Principal principal) {
        //TODO get logged in user based on SSO session params
        var username = principal.getName();
        var user = this.userRepository.findUserByUsername(username);
        var userServers = userServerService.getUserServers(user.get());
        return userServers.stream().map(UserServerView::fromUserServer);
    }

    @GetMapping(path ="/{serverId}")
    public UserServerView getServer(Principal principal, @PathVariable("serverId") Long serverId) {
        //TODO get logged in user based on SSO session params
        var username = principal.getName();
        var user = this.userRepository.findUserByUsername(username);

        var userServer = userServerService.getUserServer(user.get(), serverId);
        var server = userServer.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find server."));
        return UserServerView.fromUserServer(server);
    }

    @PostMapping
    public UserServerView AddNewUserServer(Principal principal, @RequestBody UserServerRequestBody requestBody) {
        //TODO get logged in user based on SSO session params
        var username = principal.getName();
        var user = this.userRepository.findUserByUsername(username);
        var userServer = userServerService.createServer(user.get(), requestBody.getServerUrl(), requestBody.getAccessToken());
        return UserServerView.fromUserServer(userServer);
    }

}
