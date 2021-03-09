package com.eris.gitlabanalyzer;

import com.eris.gitlabanalyzer.model.*;
import com.eris.gitlabanalyzer.repository.ProjectRepository;
import com.eris.gitlabanalyzer.repository.ServerRepository;
import com.eris.gitlabanalyzer.repository.UserRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.OffsetDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ModelTests<SERVER_URL> {

    @Autowired
    private ServerRepository serverRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProjectRepository projectRepository;

    private String SERVER_URL="http://142.58.22.215";
    private String ACCESS_TOKEN="hpqTjsKz9pSSVRZPRsLE";

    private final Server testServer = new Server(SERVER_URL);
    private final User testUser = new User("csl33");
    private final UserServer testUserServer = new UserServer(testUser, testServer, ACCESS_TOKEN);
    private final Project testProject = new Project(25514L, "TestProject", "Test Proejct",
            "http:wow!", testServer);
    private final GitManagementUser testGitManagementUser = new GitManagementUser(1L,"csl33", "Jason Lee", testServer);
    private final Commit testCommit = new Commit("SHA", "Title", "csl33", "csl33@sfu.ca",
            OffsetDateTime.parse("2016-10-05T08:20:10+05:30"), "URL", testProject);
    private final CommitComment testCommitComment = new CommitComment(testGitManagementUser, testCommit,
            "URL", OffsetDateTime.parse("2014-12-12T08:20:10+05:30"));
    private final Issue testIssue = new Issue(1L, "Issue", "csl33", OffsetDateTime.parse("2014-12-12T08:20:10+05:30"), "URL", testProject, testGitManagementUser);

    @BeforeAll
    void init(){
        serverRepository.save(testServer);
        projectRepository.save(testProject);
        userRepository.save(testUser);

        // Many to Many relationship between User -> UserServer <- Sever
        testServer.addUserServer(testUserServer);
        testUser.addUserServer(testUserServer);
        // Add project to server
        testServer.addProject(testProject);
        // Add member to project
        testProject.addGitManagementUser(testGitManagementUser);
        // Connect project to GitManagementUser
        testGitManagementUser.addProject(testProject);
        // Connect commit to project.
        testProject.addCommit(testCommit);
        // Connect commit to GitManagementUser
        testGitManagementUser.addCommit(testCommit);
        // Connect CommitComment to Commit
        testCommit.addCommitComment(testCommitComment);
        // Connect CommitComment to GitManagementUser
        testGitManagementUser.addCommitComment(testCommitComment);


    }

    @Test
    void serverModel() {
        Server queryResult = serverRepository.findByServerUrlAndAccessToken(SERVER_URL,
                ACCESS_TOKEN);
        assertNotNull(queryResult);
        assertEquals(queryResult.getServerUrl(), SERVER_URL);
    }

    @Test
    void userModel() {
        User queryResult = userRepository.findUserByUsername("csl33");
        assertNotNull(queryResult);
        assertEquals(queryResult.getUsername(), "csl33");
    }

    /* From what I can tell this test can't lazy load as the userServer model is initialized and given
    to server and user but the userServer model isn't actually saved in any repository so
    the lazy fetch fails */
    //Todo fix once data can be saved to database
   /* @Test
    void userServerModel() {
        Server serverQueryResult = serverRepository.findByServerUrlAndAccessToken(SERVER_URL,
                ACCESS_TOKEN);
        User userQueryResult = userRepository.findUserByUsername("csl33");
        UserServer userServerOfServer = serverQueryResult.getUserServers().get(0);
        UserServer userServerOfUser = userQueryResult.getUserServers().get(0);
        assertNotNull(userServerOfServer);
        assertNotNull(userServerOfUser);
        assertEquals(userServerOfServer, userServerOfUser);
    } */

    @Test
    void projectModel() {
        Project queryResult= projectRepository.findById(1L).orElse(null);
        assertNotNull(queryResult);
        assertEquals(testProject.getId(), queryResult.getId());
        assertEquals(testProject.getGitLabProjectId(), queryResult.getGitLabProjectId());
        assertEquals(testProject.getName(), queryResult.getName());
        assertEquals(testProject.getNameWithNamespace(), queryResult.getNameWithNamespace());
        assertEquals(testProject.getWebUrl(), queryResult.getWebUrl());
    }
}
