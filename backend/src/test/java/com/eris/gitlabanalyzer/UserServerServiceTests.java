package com.eris.gitlabanalyzer;

import com.eris.gitlabanalyzer.repository.UserRepository;
import com.eris.gitlabanalyzer.service.GitLabService;
import com.eris.gitlabanalyzer.service.UserServerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServerServiceTests {

    @Value("${gitlab.SERVER_URL}")
    String serverUrl;

    @Value("${gitlab.ACCESS_TOKEN}")
    String accessToken;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserServerService userServerService;

    private GitLabService gitLabService =  new GitLabService(serverUrl, accessToken);


    @Test
    void testGetUserServer()  {
        var serverId = 1l;
        var user = userRepository.findById(1l).get();
        var userServer = userServerService.getUserServer(user, serverId);
        assertTrue(userServer.isPresent());
        var userServerValue = userServer.get();
        assertEquals(serverId, userServerValue.getServer().getId());
    }

    @Test
    void testGetUserServers()  {
        var user = userRepository.findById(1l).get();
        var userServers = userServerService.getUserServers(user);
        assertEquals(1, userServers.size());
        assertEquals(1l, userServers.get(0).getServer().getId());
    }

    @Test
    @Transactional
    void testCreateDuplicateUserServer()  {
        var user = userRepository.findById(1l).get();
        var userServers = userServerService.getUserServers(user);
        var userServer = userServers.get(0);
        assertThrows(IllegalStateException.class,
                () -> {userServerService.createServer(user, userServer.getServer().getServerUrl(), "diff_access_token_than_the_original");});
    }
}
