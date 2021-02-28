package com.eris.gitlabanalyzer.service;

import com.eris.gitlabanalyzer.model.*;
import com.eris.gitlabanalyzer.repository.ServerRepository;
import com.eris.gitlabanalyzer.repository.UserServerRepository;
import com.eris.gitlabanalyzer.viewmodel.ServerAccess;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServerService {
    private final ServerRepository serverRepository;
    private final UserServerRepository userServerRepository;

    public UserServerService(ServerRepository serverRepository, UserServerRepository userServerRepository) {
        this.serverRepository = serverRepository;
        this.userServerRepository = userServerRepository;
    }

    public Server getServer(Long serverId) {
        return serverRepository.getOne(serverId);
    }

    public List<Server> getUserServers(User user) {
        return serverRepository.findByServerUserId(user.getId());
    }

    public void createServer(User user, ServerAccess serverAccess) {
        Optional<Server> serverByUser = serverRepository.findByServerUrlAndUserId(serverAccess.getServerUrl(), user.getId());
        if (serverByUser.isPresent()) {
            throw new IllegalStateException("Server already registered");
        }

        UserServer userServer = new UserServer(serverAccess.getAccessToken());
        userServer.setUser(user);

        Optional<Server> ServerByUrl = serverRepository.findFirstByServerUrl(serverAccess.getServerUrl());
        if (ServerByUrl.isPresent()) {
            userServer.setServer(ServerByUrl.get());
        }
        else {
            var server = serverRepository.save(new Server(serverAccess.getServerUrl()));
            userServer.setServer(server);
        }
        userServerRepository.save(userServer);
    }

}
