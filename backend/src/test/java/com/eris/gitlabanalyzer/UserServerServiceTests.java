package com.eris.gitlabanalyzer;

import com.eris.gitlabanalyzer.repository.UserRepository;
import com.eris.gitlabanalyzer.service.UserServerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServerServiceTests {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserServerService userServerService;


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
                () -> {userServerService.createUserServer(user, userServer.getServer().getServerUrl(), "diff_access_token_than_the_original");});
    }
}
