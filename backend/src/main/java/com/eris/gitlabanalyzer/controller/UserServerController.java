package com.eris.gitlabanalyzer.controller;

import com.eris.gitlabanalyzer.repository.UserRepository;
import com.eris.gitlabanalyzer.service.UserServerService;
import com.eris.gitlabanalyzer.viewmodel.UserServerRequestBody;
import com.eris.gitlabanalyzer.viewmodel.UserServerView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
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

    //TODO change to userserver
    @GetMapping
    public Stream<UserServerView> getUserServers(Principal principal) {
        //TODO get logged in user based on SSO session params
        var username = principal.getName();
        var user = this.userRepository.findUserByUsername(username);
        var userServers = userServerService.getUserServers(user);
        return userServers.stream().map(UserServerView::fromUserServer);
    }

    //TODO change to userserver
    @GetMapping(path ="/{serverId}")
    public UserServerView getServer(@PathVariable("serverId") Long serverId) {
        var userServer = userServerService.getUserServer(serverId);
        return UserServerView.fromUserServer(userServer);
    }

    //TODO change to return userserver
    @PostMapping
    public UserServerView AddNewUserServer(Principal principal, @RequestBody UserServerRequestBody requestBody) {
        //TODO get logged in user based on SSO session params
        var username = principal.getName();
        var user = this.userRepository.findUserByUsername(username);
        var userServer = userServerService.createServer(user, requestBody.getServerUrl(), requestBody.getAccessToken());
        return UserServerView.fromUserServer(userServer);
    }

}
