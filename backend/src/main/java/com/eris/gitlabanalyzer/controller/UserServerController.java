package com.eris.gitlabanalyzer.controller;

import com.eris.gitlabanalyzer.model.Server;
import com.eris.gitlabanalyzer.model.User;
import com.eris.gitlabanalyzer.service.UserServerService;
import com.eris.gitlabanalyzer.viewmodel.ServerAccess;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/servers")
public class UserServerController {
    private final UserServerService userServerService;
    @Autowired
    public UserServerController(UserServerService userServerService){
        this.userServerService = userServerService;
    }

    //TODO change to userserver
    @GetMapping
    public List<Server> getUserServers() {
        //TODO get logged in user
        var user = new User(1l, "TestUser");
        return userServerService.getUserServers(user);
    }

    //TODO change to userserver
    @GetMapping(path ="/{serverId}")
    public Server getServer(@PathVariable("serverId") Long serverId) {
        return userServerService.getServer(serverId);
    }

    //TODO change to return userserver
    @PostMapping
    public void AddNewUserServer(@RequestBody ServerAccess serverAccess) {
        //TODO get logged in user
        var user = new User(1l, "TestUser");
        userServerService.createServer(user, serverAccess);
    }

}
