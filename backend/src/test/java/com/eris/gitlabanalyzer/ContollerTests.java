package com.eris.gitlabanalyzer;

import com.eris.gitlabanalyzer.controller.GitLabController;
import com.eris.gitlabanalyzer.controller.ProjectController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class controllerTests {

    @Autowired
    private GitLabController gitLabController;
    @Autowired
    private ProjectController projectController;


    @Test
    void contextLoads() {
        assertThat(gitLabController).isNotNull();
        assertThat(projectController).isNotNull();
    }

}
