package com.eris.gitlabanalyzer.service;

import com.eris.gitlabanalyzer.model.Server;
import com.eris.gitlabanalyzer.model.User;
import com.eris.gitlabanalyzer.model.UserServer;
import com.eris.gitlabanalyzer.repository.ServerRepository;
import com.eris.gitlabanalyzer.repository.UserServerRepository;
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

    public Optional<UserServer> getUserServer(User user, Long serverId) {
        var userServers = this.getUserServers(user);
        return userServers.stream().filter(s -> s.getServer().getId() == serverId).findFirst();
    }

    public List<UserServer> getUserServers(User user) {
        return userServerRepository.findUserServerByUserId(user.getId());
    }

    public UserServer createServer(User user, String serverUrl, String accessToken) {
        Optional<Server> serverByUser = serverRepository.findByServerUrlAndUserId(serverUrl, user.getId());
        if (serverByUser.isPresent()) {
            throw new IllegalStateException("Server already registered.");
        }

        UserServer userServer = new UserServer(accessToken);
        userServer.setUser(user);

        Optional<Server> ServerByUrl = serverRepository.findByServerUrl(serverUrl);
        if (ServerByUrl.isPresent()) {
            userServer.setServer(ServerByUrl.get());
        }
        else {
            var server = serverRepository.save(new Server(serverUrl));
            userServer.setServer(server);
        }
        return userServerRepository.save(userServer);
    }

}
