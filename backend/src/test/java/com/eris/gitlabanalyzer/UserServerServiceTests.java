package com.eris.gitlabanalyzer;

import com.eris.gitlabanalyzer.repository.UserRepository;
import com.eris.gitlabanalyzer.service.UserServerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
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

    @Test
    void testUpdateUserServers()  {
        var user = userRepository.findById(1l).get();
        var newAccessToken = "7AX1GEU5hzuyF1NYNRyX";
        var userServer = userServerService.getUserServer(user, 1L).get();
        var oldAccessToken = userServer.getAccessToken();

        userServerService.updateUserServer(user, userServer.getServer().getId(), newAccessToken);
        var updatedUserServer = userServerService.getUserServer(user, userServer.getServer().getId()).get();
        assertEquals(newAccessToken, updatedUserServer.getAccessToken());

        userServerService.updateUserServer(user, userServer.getServer().getId(), oldAccessToken);
        updatedUserServer = userServerService.getUserServer(user, userServer.getServer().getId()).get();
        assertEquals(oldAccessToken, updatedUserServer.getAccessToken());
    }

    @Test
    void testDeleteUserServers()  {
        var user = userRepository.findById(1l).get();
        var userServer = userServerService.createUserServer(user, "https://csil-git1.cs.surrey.sfu.ca","a_gnyeKkJjiKyGYZW1JD");
        var userServers = userServerService.getUserServers(user);
        assertEquals(2, userServers.size());
        userServerService.deleteUserServer(user, userServer.getServer().getId());
        assertEquals(1l, userServers.get(0).getServer().getId());
    }
}
