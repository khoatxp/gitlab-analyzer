package com.eris.gitlabanalyzer.service;

import com.eris.gitlabanalyzer.model.*;
import com.eris.gitlabanalyzer.repository.ServerRepository;
import com.eris.gitlabanalyzer.repository.UserServerRepository;
import org.jsoup.Jsoup;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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

    public UserServer createUserServer(User user, String serverUrl, String accessToken) {
        String trimmedUrl = sanitizeUrl(serverUrl);
        String trimmedToken = trimAndStripTags(accessToken);
        Optional<Server> serverByUser = serverRepository.findByServerUrlAndUserId(trimmedUrl, user.getId());
        if (serverByUser.isPresent()) {
            throw new IllegalStateException("Server already registered.");
        }
        GitLabService gitLabService = new GitLabService(trimmedUrl, trimmedToken);
        if (!gitLabService.validateAccessToken()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Given URL or the access token is not valid.");
        }
        UserServer userServer = new UserServer(trimmedToken);
        userServer.setUser(user);

        Optional<Server> ServerByUrl = serverRepository.findByServerUrl(trimmedUrl);
        if (ServerByUrl.isPresent()) {
            userServer.setServer(ServerByUrl.get());
        }
        else {
            var server = serverRepository.save(new Server(trimmedUrl));
            userServer.setServer(server);
        }
        return userServerRepository.save(userServer);
    }

    public UserServer updateUserServer(User user, Long serverId, String accessToken) {
        var userServer = userServerRepository.findUserServerByUserIdAndServerId(user.getId(), serverId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User server not found."));
        var server = userServer.getServer();
        String serverUrl =  server.getServerUrl();
        String trimmedToken = trimAndStripTags(accessToken);
        GitLabService gitLabService = new GitLabService(serverUrl, trimmedToken);
        if (!gitLabService.validateAccessToken()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Given access token is not valid.");
        }
        userServer.setAccessToken(trimmedToken);
        return userServerRepository.save(userServer);
    }

    public void deleteUserServer(User user, Long serverId) {
        var userServer = userServerRepository.findUserServerByUserIdAndServerId(user.getId(), serverId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User server not found."));
        userServerRepository.delete(userServer);
    }

    public String trimAndStripTags(String string) {
        if (string == null || string.length() == 0) {
            return string;
        }
        String result = string.trim();
        result = Jsoup.parse(result).text();
        return result;
    }

    public String sanitizeUrl(String url) {
        String trimmed = trimAndStripTags(url);
        while(trimmed.endsWith("/")) {
            trimmed = trimmed.substring(0, trimmed.length() - 1);
        }
        return trimmed;
    }
}
