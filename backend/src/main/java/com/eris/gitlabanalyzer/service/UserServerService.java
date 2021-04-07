package com.eris.gitlabanalyzer.service;

import com.eris.gitlabanalyzer.model.*;
import com.eris.gitlabanalyzer.repository.ServerRepository;
import com.eris.gitlabanalyzer.repository.UserServerRepository;
import com.eris.gitlabanalyzer.viewmodel.UserServerRequestBody;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
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
        Optional<Server> serverByUser = serverRepository.findByServerUrlAndUserId(serverUrl, user.getId());
        if (serverByUser.isPresent()) {
            throw new IllegalStateException("Server already registered.");
        }
        if (!validateAccessToken(serverUrl, accessToken)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Given Access Token is not valid.");
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

    public UserServer updateUserServer(User user, Long serverId, String accessToken) {
        var server = serverRepository.findServerById(serverId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Server not found."));
        String serverUrl =  server.getServerUrl();
        if (!validateAccessToken(serverUrl, accessToken)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Given Access Token is not valid.");
        }
        var userServer = userServerRepository.findUserServerByUserIdAndServerId(user.getId(), serverId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User server not found."));
        userServer.setAccessToken(accessToken);
        return userServerRepository.save(userServer);
    }

    public void deleteUserServer(User user, Long serverId) {
        var userServer = userServerRepository.findUserServerByUserIdAndServerId(user.getId(), serverId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User server not found."));
        userServerRepository.delete(userServer);
    }

    public boolean validateAccessToken(String serverUrl, String accessToken) {
        HttpURLConnection connection = null;
        try {
            String request = "/api/v4/user";
            System.out.println(serverUrl + request);
            URL url = new URL(serverUrl + request);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("PRIVATE-TOKEN", accessToken);
            connection.setRequestMethod("GET");
            connection.connect();
            int code = connection.getResponseCode();
            if ((200 <= code) && (code <= 299)) {
                connection.disconnect();
                return true;
            }
        } catch (ProtocolException e) {
            e.printStackTrace();
            connection.disconnect();
            return false;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            connection.disconnect();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            connection.disconnect();
            return false;
        }
        connection.disconnect();
        return false;
    }
}
